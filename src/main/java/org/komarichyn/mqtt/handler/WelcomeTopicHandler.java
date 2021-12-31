package org.komarichyn.mqtt.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.komarichyn.mqtt.IMqttHandler;
import org.komarichyn.mqtt.MqttPublisher;
import org.komarichyn.ss.api.dto.IDevice;
import org.komarichyn.ss.api.dto.SensorDto;
import org.komarichyn.ss.service.IDeviceRegistrationService;
import org.komarichyn.ss.service.IDeviceService;
import org.komarichyn.ss.service.ISensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WelcomeTopicHandler implements IMqttHandler {

  public static final String WELCOME = "welcome";
  public static final String INCOME_TEMPLATE = "device/%s/income";
  public static final String OUTCOME_TEMPLATE = "device/%s/outcome";

  @Autowired
  private IDeviceService deviceService;
  @Autowired
  private ISensorService sensorService;
  @Autowired
  private IDeviceRegistrationService registrationService;
  @Autowired
  private ObjectMapper objectMapper ;
  @Autowired
  private MqttPublisher publisher;

  private String topic;

  public void setTopic(String topic) {
    this.topic = topic;
  }

  public String getTopic() {
    return this.topic;
  }

  @Override
  public void handleMessage(String topic, MqttMessage message) {
    if(WELCOME.equals(topic)){
      //todo handle there
      log.info("got message: {} from topic: {}", message, topic);
      IDevice device = null;

      try {
        device = deviceService.parseJsonToDevice(new String(message.getPayload()));
      } catch (JsonProcessingException e) {
        log.error(e.getMessage(), e);
      }

      if(device == null){
        log.warn("could not recognize device by message: {}", message);
        return;
      }

      String code = device.getCode();
      SensorDto sensorDto = sensorService.getSensor(code);

      if(sensorDto == null){
        sensorDto = new SensorDto();
        sensorDto.setName(code);
        sensorDto.setIncome(String.format(INCOME_TEMPLATE, code));
        sensorDto.setOutcome(String.format(OUTCOME_TEMPLATE, code));
        sensorDto = sensorService.save(sensorDto);
      }

      device.setSensor(sensorDto);

      SensorDto config = registrationService.registration(device);
      log.debug("registration data:{}", config);

      if (config != null) {
        try {
          publisher.publish(sensorDto.getIncome(), device, 0);
        } catch (MqttException e) {
          log.error(e.getMessage(), e);
        }
      }
    }
  }
}
