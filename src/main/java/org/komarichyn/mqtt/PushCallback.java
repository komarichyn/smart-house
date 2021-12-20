package org.komarichyn.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PushCallback implements MqttCallback {

  @Autowired
  private MqttConfig mqttConfig;

  private static MqttClient client;

  @Override
  public void connectionLost(Throwable throwable) {
    // After the connection is lost, it is usually reconnected here
    log.info("Disconnected, can be reconnected");
    if (null != client) {
      mqttConfig.getMqttPushClient();
    }
  }

  @Override
  public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
    // The message you get after you subscribe will be executed here
    log.info("Receive message subject : " + topic);
    log.info("receive messages Qos : " + mqttMessage.getQos());
    log.info("Receive message content : " + new String(mqttMessage.getPayload()));
  }

  @Override
  public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
    log.info("deliveryComplete---------" + iMqttDeliveryToken.isComplete());
  }

}
