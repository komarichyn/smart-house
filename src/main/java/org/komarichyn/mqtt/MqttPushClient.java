package org.komarichyn.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MqttPushClient {

  @Autowired
  private PushCallback pushCallback;

  private static MqttClient client;

  private static MqttClient getClient() {
    return client;
  }

  private static void setClient(MqttClient client) {
    MqttPushClient.client = client;
  }

  /**
   * Client connection
   *
   * @param host      ip+port
   * @param clientID  Client Id
   * @param username  User name
   * @param password  Password
   * @param timeout   Timeout time
   * @param keepalive Retention number
   */
  public void connect(String host, String clientID, String username, String password, int timeout, int keepalive) {
    MqttClient client;
    try {
      client = new MqttClient(host, clientID, new MemoryPersistence());
      MqttConnectOptions options = new MqttConnectOptions();
      options.setCleanSession(true);
      options.setUserName(username);
      options.setPassword(password.toCharArray());
      options.setConnectionTimeout(timeout);
      options.setKeepAliveInterval(keepalive);
      MqttPushClient.setClient(client);
      try {
        client.setCallback(pushCallback);
        client.connect(options);
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }



  /**
   * Release
   *
   * @param qos         Connection mode
   * @param retained    Whether to retain
   * @param topic       theme
   * @param pushMessage Message body
   */
  public void publish(int qos, boolean retained, String topic, String pushMessage) {
    MqttMessage message = new MqttMessage();
    message.setQos(qos);
    message.setRetained(retained);
    message.setPayload(pushMessage.getBytes());
    MqttTopic mTopic = MqttPushClient.getClient().getTopic(topic);
    if (null == mTopic) {
      log.error("topic not exist");
      return;
    }
    MqttDeliveryToken token;
    try {
      token = mTopic.publish(message);
      token.waitForCompletion();
    } catch (MqttPersistenceException e) {
      log.error("some issue with persistence");
      log.error(e.getMessage(), e);
    } catch (MqttException e) {
      log.error(e.getMessage(), e);
    }
  }

  /**
   * Subscribe to a topic
   *
   * @param topic theme
   * @param qos   Connection mode
   */
  public void subscribe(String topic, int qos) {
    log.info("Start subscribing to topics {}" , topic);
    try {
      MqttPushClient.getClient().subscribe(topic, qos);
    } catch (MqttException e) {
      log.error(e.getMessage(), e);
    }
  }

}
