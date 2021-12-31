package org.komarichyn.mqtt;

import org.eclipse.paho.client.mqttv3.MqttMessage;

public interface IMqttHandler {


  void handleMessage(String topic, MqttMessage message);

  default void handle(String topic, MqttMessage message){
    handleMessage(topic, message);
  }

}
