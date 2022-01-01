#include <Arduino.h>

#include <PubSubClient.h>
#include <ArduinoJson.h>
#include "WiFi.h"


const char* ssid = "MK_plus";
const char* password = "mykhaylo";

// MQTT Broker
const char *mqttServer = "192.168.3.7";
int mqttPort = 1883;
const char *topic = "welcome";
String incomeTopic;

//const char *mqtt_username = "emqx";
//const char *mqtt_password = "public";

WiFiClient espClient;
PubSubClient mqttClient(espClient);

String getUID() {
    char ssid1[15];
    char ssid2[15];

    uint64_t chipid = ESP.getEfuseMac(); // The chip ID is essentially its MAC address(length: 6 bytes).
    uint16_t chip = (uint16_t)(chipid >> 32);

    snprintf(ssid1, 15, "%04X", chip);
    snprintf(ssid2, 15, "%08X", (uint32_t)chipid);

    return  String(ssid1) +  String(ssid2);
}

void callback(char* topic, byte* payload, unsigned int length) {
    Serial.print("Callback - ");
    Serial.print("Message:");
    for (int i = 0; i < length; i++) {
        Serial.print((char)payload[i]);
    }

    if ((String)topic == (String)incomeTopic){
        Serial.println("\ngot configuration data");
    }
}

String setupMQTT() {
    mqttClient.setServer(mqttServer, mqttPort);
    // set the callback function
    mqttClient.setCallback(callback);
    String client_id = "esp32-client-" + getUID();
    return client_id;
}

void registration(){
    DynamicJsonDocument  doc(200);

    doc["code"] = String(getUID());
    String result;
    serializeJson(doc, result);
    Serial.println();
    serializeJsonPretty(doc, Serial);

    mqttClient.publish(topic, result.c_str());
}


void setup() {
// write your initialization code here
    Serial.begin(115200);

    // connecting to a WiFi network
    WiFi.begin(ssid, password);
    while (WiFi.status() != WL_CONNECTED) {
        delay(500);
        Serial.println("Connecting to WiFi..");
    }
    Serial.println("Connected to the WiFi network");

    incomeTopic = String("device/" + getUID() + "/income");

    Serial.println("income topic:" + String(incomeTopic));

    while (!mqttClient.connected()){
        String client_id = setupMQTT();
        if(mqttClient.connect(client_id.c_str())){
            Serial.println("mqtt broker connected");
        }else{
            Serial.print("failed with state ");
            Serial.print(mqttClient.state());
            delay(2000);
        }
    }

    mqttClient.subscribe(incomeTopic.c_str());
    mqttClient.loop();
    registration();
}



void loop() {
// write your code here
    mqttClient.loop();
}