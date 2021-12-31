package org.komarichyn.mqtt.handler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.komarichyn.mqtt.IMqttHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class IncomeTopicHandler implements IMqttHandler {

  public final String TOPIC = "device/[^\\s]*/income";
  Pattern p = Pattern.compile(TOPIC, Pattern.CASE_INSENSITIVE);

  public void init(){

  }

  @Override
  public void handleMessage(String topic, MqttMessage message) {
    Matcher m = p.matcher(topic);
    if(m.matches()){
      log.info("got message: {} from topic: {}", new String(message.getPayload()), topic);
      String deviceName = topic.substring(7, topic.length() - 7);
      log.info("device name: {}", deviceName);
    }
  }
}
