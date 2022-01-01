#include <Arduino.h>

#include <PubSubClient.h>
#include <ArduinoJson.h>
#include "WiFi.h"
#include <AHT10.h>


const char *ssid = "MK_plus";
const char *password = "mykhaylo";

// MQTT Broker
const char *mqttServer = "192.168.3.7";
int mqttPort = 1883;
const char *topic = "welcome";
String incomeTopic;

//const char *mqtt_username = "emqx";
//const char *mqtt_password = "public";

WiFiClient espClient;
PubSubClient mqttClient(espClient);


AHT10 mySensor;
#define uS_TO_S_FACTOR 1000000  /* Conversion factor for micro seconds to seconds */
#define TIME_TO_SLEEP  (60 * 10)        /* Time ESP32 will go to sleep (in seconds) */

RTC_DATA_ATTR int bootCount = 0;

struct TAH_t {
    float temperature = 0;
    float humidity = 0;
    long lastRead = 0;// = millis();
};

TAH_t data = TAH_t();

/*
Method to print the reason by which ESP32
has been awaken from sleep
*/
void print_wakeup_reason() {
    esp_sleep_wakeup_cause_t wakeup_reason;

    wakeup_reason = esp_sleep_get_wakeup_cause();

    switch (wakeup_reason) {
        case ESP_SLEEP_WAKEUP_EXT0 :
            Serial.println("Wakeup caused by external signal using RTC_IO");
            break;
        case ESP_SLEEP_WAKEUP_EXT1 :
            Serial.println("Wakeup caused by external signal using RTC_CNTL");
            break;
        case ESP_SLEEP_WAKEUP_TIMER :
            Serial.println("Wakeup caused by timer");
            break;
        case ESP_SLEEP_WAKEUP_TOUCHPAD :
            Serial.println("Wakeup caused by touchpad");
            break;
        case ESP_SLEEP_WAKEUP_ULP :
            Serial.println("Wakeup caused by ULP program");
            break;
        default :
            Serial.printf("Wakeup was not caused by deep sleep: %d\n", wakeup_reason);
            break;
    }
}


String getUID() {
    char ssid1[15];
    char ssid2[15];

    uint64_t chipid = ESP.getEfuseMac(); // The chip ID is essentially its MAC address(length: 6 bytes).
    uint16_t chip = (uint16_t) (chipid >> 32);

    snprintf(ssid1, 15, "%04X", chip);
    snprintf(ssid2, 15, "%08X", (uint32_t) chipid);

    return String(ssid1) + String(ssid2);
}

void sendTemperatureAndHumidity(String topicForData){
    long time = millis();
    Serial.println("current time:" + String(time));
    Serial.println("lastRead:" + String(data.lastRead));
    Serial.println("topic:" + topicForData);
    uint8_t readStatus = mySensor.readRawData();
    if (readStatus != AHT10_ERROR) {
        float temp = mySensor.readTemperature(AHT10_USE_READ_DATA);
        float humi = mySensor.readHumidity(AHT10_USE_READ_DATA);
        data.humidity = humi;
        data.temperature = temp;

        Serial.println("temp:" + String(temp));
        Serial.println("hum:" + String(humi));

        DynamicJsonDocument  doc(200);

        doc["temperature"] = String(temp);
        doc["humidity"] = String(humi);
        String result;
        serializeJson(doc, result);
        Serial.println();
        serializeJsonPretty(doc, Serial);

        mqttClient.publish(topicForData.c_str(), result.c_str());
    }
    Serial.println("--");
    vTaskDelay(pdMS_TO_TICKS(5000));

    Serial.println("Going to sleep now");
    Serial.flush();
    esp_deep_sleep_start();
}

void callback(char *topic, byte *payload, unsigned int length) {
    Serial.print("Callback - ");
    Serial.print("Message:");
    for (int i = 0; i < length; i++) {
        Serial.print((char) payload[i]);
    }

    if ((String) topic == (String) incomeTopic) {
        Serial.println("\ngot configuration data");
        DynamicJsonDocument doc(200);
        deserializeJson(doc, payload);
        const char* sensor = doc["name"];
        const char* outcome = doc["outcome"];
        const char* income = doc["income"];
        sendTemperatureAndHumidity(String(outcome));
    }
}

String setupMQTT() {
    mqttClient.setServer(mqttServer, mqttPort);
    // set the callback function
    mqttClient.setCallback(callback);
    String client_id = "esp32-client-" + getUID();
    return client_id;
}

void registration() {
    DynamicJsonDocument doc(200);

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

    //Increment boot number and print it every reboot
    ++bootCount;
    Serial.println("Boot number: " + String(bootCount));

    print_wakeup_reason();

    /**
     * First we configure the wake up source
     * We set our ESP32 to wake up every 5 seconds
     */
    esp_sleep_enable_timer_wakeup(TIME_TO_SLEEP * uS_TO_S_FACTOR);
    Serial.println("Setup ESP32 to sleep for every " + String(TIME_TO_SLEEP) + " Seconds");

    mySensor = AHT10(AHT10_ADDRESS_0X38);
    while (!mySensor.begin()) {
        Serial.println("AHT10 not connected or fail to load calibration coefficient");
        vTaskDelay(pdMS_TO_TICKS(5000));
    }
    Serial.println("AHT10 OK");
    // Set WiFi to station mode and disconnect from an AP if it was previously connected

    // connecting to a WiFi network
    WiFi.begin(ssid, password);
    while (WiFi.status() != WL_CONNECTED) {
        delay(500);
        Serial.println("Connecting to WiFi..");
    }
    Serial.println("Connected to the WiFi network");

    incomeTopic = String("device/" + getUID() + "/income");

    Serial.println("income topic:" + String(incomeTopic));

    while (!mqttClient.connected()) {
        String client_id = setupMQTT();
        if (mqttClient.connect(client_id.c_str())) {
            Serial.println("mqtt broker connected");
        } else {
            Serial.print("failed with state ");
            Serial.print(mqttClient.state());
            delay(2000);
        }
    }

    Serial.println("subscribe to :" + incomeTopic);
    mqttClient.subscribe(incomeTopic.c_str());
    mqttClient.loop();
    Serial.println("start registration");
    registration();
    Serial.println("end registration");
}


void loop() {
// write your code here
    mqttClient.loop();
}