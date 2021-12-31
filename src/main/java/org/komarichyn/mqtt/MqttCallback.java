package org.komarichyn.mqtt;

import java.util.List;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.komarichyn.ss.service.IDeviceRegistrationService;
import org.komarichyn.ss.service.ISensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MqttCallback implements org.eclipse.paho.client.mqttv3.MqttCallback {

  @Autowired
  public List<IMqttHandler> handlers;

  @Autowired
  private IMqttClient mqttClient;
  @Autowired
  private MqttConnectOptions connectionOptions;
  @Autowired
  private IDeviceRegistrationService registrationService;
  @Autowired
  private ISensorService sensorService;

  @PostConstruct
  public void connect() {
    log.info("connect to mqtt broker");
    try {
      mqttClient.setCallback(this);
      if(!mqttClient.isConnected()) {
        mqttClient.connect(connectionOptions);
      }
      mqttClient.subscribe("#");
      log.info("connection passed success");
    } catch (MqttException e) {
      log.error(e.getMessage(), e);
    }
  }

  @Override
  public void connectionLost(Throwable cause) {
    log.warn("connection lost. try to reconnect");
    this.connect();
  }

  @Override
  public void messageArrived(String topic, MqttMessage message)  {
    // The message you get after you subscribe will be executed here
    log.info("Receive message subject : " + topic);
    log.info("receive messages Qos : " + message.getQos());
    log.info("Receive message content : " + new String(message.getPayload()));

    log.debug("pass message to handler");
    handlers.forEach(handler -> {
      log.debug("handle topic name:{}, by handler: {}", topic, handler.getClass());
      handler.handle(topic, message);
    });

//    if(DEFAULT_TOPIC.equals(topic)){
//      ObjectMapper mapper = new ObjectMapper();
//      IDevice device = mapper.readValue(message.getPayload(), DeviceDto.class);
//      SensorDto sensorDto = sensorService.getSensor(device.getCode());
//
//      if(sensorDto == null){
//        sensorDto = new SensorDto();
//        sensorDto.setName(device.getCode());
//        sensorDto.setIncome("device/" + device.getCode() + "/income");
//        sensorDto.setOutcome("device/" + device.getCode() + "/outcome");
//        sensorDto = sensorService.save(sensorDto);
//      }
//
//      device.setSensor(sensorDto);
//      SensorDto config = registrationService.registration(device);
//      log.debug("registration data:{}", config);
//
////      //todo send configuration to client and subscribe to proper topic
////      if(config != null) {
////        String data = mapper.writeValueAsString(config);
////        log.debug("result data: {}", data);
////        mqttConfig.getMqttPushClient().subscribe(config.getOutcome(), 0);
////        mqttConfig.getMqttPushClient().publish(0, false, config.getIncome(), data);
////      }
//    }
  }

  @Override
  public void deliveryComplete(IMqttDeliveryToken token) {
    log.info("deliveryComplete---------{}", token.isComplete());
  }
}
