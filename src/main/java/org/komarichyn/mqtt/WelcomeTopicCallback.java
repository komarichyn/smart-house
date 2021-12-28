package org.komarichyn.mqtt;

import static org.komarichyn.ss.service.IMQTTService.DEFAULT_TOPIC;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.komarichyn.ss.api.dto.DeviceDto;
import org.komarichyn.ss.api.dto.IDevice;
import org.komarichyn.ss.api.dto.SensorDto;
import org.komarichyn.ss.service.IDeviceRegistrationService;
import org.komarichyn.ss.service.ISensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class WelcomeTopicCallback implements MqttCallback {

  @Autowired
  private MqttConfig mqttConfig;
  @Autowired
  private IDeviceRegistrationService registrationService;
  @Autowired
  private ISensorService sensorService;

  private static MqttClient client;

  @Override
  public void connectionLost(Throwable throwable) {
    // After the connection is lost, it is usually reconnected here
    log.info("Disconnected, can be reconnected");
    mqttConfig.getMqttPushClient();
  }

  @Override
  public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
    // The message you get after you subscribe will be executed here
    log.info("Receive message subject : " + topic);
    log.info("receive messages Qos : " + mqttMessage.getQos());
    log.info("Receive message content : " + new String(mqttMessage.getPayload()));

    if(DEFAULT_TOPIC.equals(topic)){
      ObjectMapper mapper = new ObjectMapper();
      IDevice device = mapper.readValue(mqttMessage.getPayload(), DeviceDto.class);
      SensorDto sensorDto = sensorService.getSensor(device.getCode());

      if(sensorDto == null){
        sensorDto = new SensorDto();
        sensorDto.setName(device.getCode());
        sensorDto.setIncome("device/" + device.getCode() + "/income");
        sensorDto.setOutcome("device/" + device.getCode() + "/outcome");
        sensorDto = sensorService.save(sensorDto);
      }

      device.setSensor(sensorDto);
      SensorDto config = registrationService.registration(device);
      log.debug("registration data:{}", config);

      //todo send configuration to client and subscribe to proper topic
      if(config != null) {
        String data = mapper.writeValueAsString(config);
        log.debug("result data: {}", data);
        mqttConfig.getMqttPushClient().subscribe(config.getOutcome(), 0);
        mqttConfig.getMqttPushClient().publish(0, false, config.getIncome(), data);
      }
    }
  }

  @Override
  public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
    log.info("deliveryComplete---------" + iMqttDeliveryToken.isComplete());
  }

}
