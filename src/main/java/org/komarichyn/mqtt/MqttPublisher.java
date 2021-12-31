package org.komarichyn.mqtt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MqttPublisher {
  @Autowired
  private IMqttClient mqttClient;
  @Autowired
  private MqttConnectOptions connectionOptions;
  @Autowired
  private ObjectMapper objectMapper;

  @PostConstruct
  public void connect() {
    log.info("connect to mqtt broker");
    try {
      if(!mqttClient.isConnected()) {
        mqttClient.connect(connectionOptions);
      }
      log.info("connection passed success");
    } catch (MqttException e) {
      log.error(e.getMessage(), e);
    }
  }


  public void publish(String topic, String message, int qos) throws MqttException {
    log.debug("publish message:{} to topic: {} with qos:{}", message, topic, qos);
    MqttMessage mqttMessage = new MqttMessage(message.getBytes(StandardCharsets.UTF_8));
    mqttMessage.setQos(qos);
    mqttClient.publish(topic, mqttMessage);
    log.debug("message was sent");
  }

  public void publish(String topic, Object message, int qos) throws MqttException {
    if (message != null) {
      String data = null;
      try {
        data = objectMapper.writeValueAsString(message);
      } catch (JsonProcessingException e) {
        log.error(e.getMessage(), e);
      }
      log.debug("result data: {}", data);
      this.publish(topic, data, qos);
    }
  }


}
