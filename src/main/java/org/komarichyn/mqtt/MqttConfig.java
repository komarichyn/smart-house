package org.komarichyn.mqtt;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@Setter
@Getter
@Slf4j
public class MqttConfig {

  @Autowired
  private MqttPushClient mqttPushClient;

  /**
   * User name
   */
  @Value("mqtt.username")
  private String username;
  /**
   * Password
   */
  @Value("mqtt.password")
  private String password;
  /**
   * Connection address
   */
  @Value("mqtt.hostUrl")
  private String hostUrl;
  /**
   * Customer Id
   */
  @Value("mqtt.clientID")
  private String clientID;
  /**
   * Default connection topic
   */
  @Value("mqtt.default-topic")
  private String defaultTopic;
  /**
   * Timeout time
   */
  @Value("mqtt.timeout")
  private int timeout;
  /**
   * Keep connected
   */
  @Value("mqtt.keepalive")
  private int keepalive;

  @Bean
  public MqttPushClient getMqttPushClient() {
    log.info("hostUrl: {}", hostUrl);
    log.info("clientID: {}", clientID);
    log.info("username: {}", username);
    log.info("password: {}", password);
    log.info("timeout: {}", timeout);
    log.info("keepalive: {}", keepalive);
    mqttPushClient.connect(hostUrl, clientID, username, password, timeout, keepalive);
    // End with / / to subscribe to all topics starting with test
    mqttPushClient.subscribe(defaultTopic, 0);
    return mqttPushClient;
  }
}
