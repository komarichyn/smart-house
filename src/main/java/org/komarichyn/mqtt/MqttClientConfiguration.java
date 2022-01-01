package org.komarichyn.mqtt;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Slf4j
@Configuration
@ComponentScan(basePackages = "org.komarichyn.mqtt")
public class MqttClientConfiguration {

  /**
   * User name
   */
  @Value("${mqtt.username}")
  private String username;
  /**
   * Password
   */
  @Value("${mqtt.password}")
  private String password;
  /**
   * Connection address
   */
  @Value("${mqtt.hostUrl}")
  private String hostUrl;
  /**
   * Customer Id
   */
  @Value("${mqtt.clientID}")
  private String clientID;
  /**
   * Default connection topic
   */
  @Value("${mqtt.default-topic}")
  private String defaultTopic;
  /**
   * Timeout time
   */
  @Value("${mqtt.timeout}")
  private int timeout;
  /**
   * Keep connected
   */
  @Value("${mqtt.keepalive}")
  private int keepalive;

  @Bean
  public IMqttClient mqttClient(MqttConnectOptions options) throws MqttException {
    IMqttClient mqttClient = new MqttClient(this.hostUrl, this.clientID, new MemoryPersistence());
    log.info("new mqtt client was created");
    return mqttClient;
  }

  @Bean
  public MqttConnectOptions mqttConnectOptions() {
    MqttConnectOptions options = new MqttConnectOptions();
    options.setAutomaticReconnect(true);
    options.setCleanSession(true);
    options.setConnectionTimeout(100);
    options.setKeepAliveInterval(100);
    log.info("configuration for mqtt:{}", options);
    return options;
  }

  @Bean
  public ObjectMapper getObjectMapper(){
    return new ObjectMapper();
  }


}
