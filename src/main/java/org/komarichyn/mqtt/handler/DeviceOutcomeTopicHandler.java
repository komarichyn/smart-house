package org.komarichyn.mqtt.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.komarichyn.mqtt.IMqttHandler;
import org.komarichyn.ss.database.sql.IRegistrationRepository;
import org.komarichyn.ss.database.sql.ISensorDataRepository;
import org.komarichyn.ss.database.sql.entity.RegistrationInfo;
import org.komarichyn.ss.database.sql.entity.Sensor;
import org.komarichyn.ss.database.sql.entity.SensorData;
import org.komarichyn.ss.service.IDeviceService;
import org.komarichyn.ss.service.ISensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DeviceOutcomeTopicHandler  implements IMqttHandler {
  public final String TOPIC = "device/[^\\s]*/outcome";
  Pattern p = Pattern.compile(TOPIC);

  @Autowired
  private ISensorService sensorService;
  @Autowired
  private ISensorDataRepository sensorDataRepository;
  @Autowired
  private IDeviceService deviceService;
  @Autowired
  private IRegistrationRepository registrationRepository;


  public void init(){

  }

  @Override
  public void handleMessage(String topic, MqttMessage message) {
    Matcher m = p.matcher(topic);
    if(m.matches()){
      log.info("got message: {} from topic: {}", new String(message.getPayload()), topic);
      String deviceName = topic.substring(7, topic.length() - 8);
      log.info("device name: {}", deviceName);
      Map<String, String> map = null;
      try {
        map = deviceService.parseJsonToDeviceData(new String(message.getPayload()));
      } catch (JsonProcessingException e) {
        log.error(e.getMessage(), e);
      }
      //fixme rework this functionality
      if(map != null) {
        String temperature = map.get("temperature");
        String humidity = map.get("humidity");
        RegistrationInfo ri = registrationRepository.findByCode(deviceName);
        Sensor sensor = ri.getSensor();

        //todo rework this functionality
        if (temperature != null && sensor != null) {
          SensorData sensorData = new SensorData();
          sensorData.setType(1);
          sensorData.setValue(temperature);
          sensorData.setSensor(sensor);
          sensorDataRepository.save(sensorData);
          log.debug("save new data :{}", sensorData);
        }
        //todo rework this functionality
        if (humidity != null && sensor != null) {
          SensorData sensorData = new SensorData();
          sensorData.setType(2);
          sensorData.setValue(humidity);
          sensorData.setSensor(sensor);
          sensorDataRepository.save(sensorData);
          log.debug("save new data :{}", sensorData);
        }
      }
    }
  }
}
