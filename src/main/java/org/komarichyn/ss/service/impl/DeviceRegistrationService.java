package org.komarichyn.ss.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.komarichyn.ss.api.dto.DeviceDto;
import org.komarichyn.ss.api.dto.IActiveDevice;
import org.komarichyn.ss.api.dto.IDevice;
import org.komarichyn.ss.api.dto.IPassiveDevice;
import org.komarichyn.ss.api.dto.SensorDto;
import org.komarichyn.ss.database.sql.IRegistrationRepository;
import org.komarichyn.ss.database.sql.entity.RegistrationInfo;
import org.komarichyn.ss.database.sql.entity.Sensor;
import org.komarichyn.ss.service.IDeviceRegistrationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DeviceRegistrationService implements IDeviceRegistrationService {

  @Autowired
  private IRegistrationRepository registrationRepository;
  @Autowired
  private ModelMapper modelMapper;


  @Override
  public boolean isActivated(IDevice device) {
    String deviceCode = device.getCode();
    RegistrationInfo registrationInfo = registrationRepository.findByCode(deviceCode);
    if (registrationInfo == null) {
      log.debug("device not found by code: {}", deviceCode);
      return false;
    }
    return registrationInfo.isActive();
  }

  @Override
  public IDevice activate(IDevice device) {
    String deviceCode = device.getCode();
    RegistrationInfo registrationInfo = registrationRepository.findByCode(deviceCode);
    if (registrationInfo == null) {
      log.debug("device not found by code: {}", deviceCode);
      return null;
    }
    if (registrationInfo.isActive()) {
      log.warn("device with code :{} already active", deviceCode);
    } else {
      registrationInfo.setActive(Boolean.TRUE);
      registrationInfo = registrationRepository.save(registrationInfo);
    }

    DeviceDto result = convert(registrationInfo);

    log.debug("device: {} was activate", result);

    return result;
  }

  @Override
  public IDevice deactivate(IDevice device) {
    String deviceCode = device.getCode();
    RegistrationInfo registrationInfo = registrationRepository.findByCode(deviceCode);
    if (registrationInfo == null) {
      log.debug("device not found by code: {}", deviceCode);
      return null;
    }
    if (registrationInfo.isActive()) {
      registrationInfo.setActive(Boolean.FALSE);
      registrationInfo = registrationRepository.save(registrationInfo);
    } else {
      log.warn("device with code :{} already deactivated", deviceCode);
    }

    DeviceDto result = convert(registrationInfo);

    log.debug("device: {} was deactivated", result);

    return result;
  }


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

  @Override
  public SensorDto registration(IDevice device) {
    String deviceCode = device.getCode();
    RegistrationInfo registrationInfo = registrationRepository.findByCode(deviceCode);

    if (registrationInfo == null) {
      registrationInfo = new RegistrationInfo();
      registrationInfo.setCode(deviceCode);
      Sensor s = new Sensor();
      s.setId(device.getSensor().getId());
      registrationInfo.setSensor(s);

//      if (device instanceof IPassiveDevice) {
//        registrationInfo.setActive(Boolean.FALSE);
//      }
//      if (device instanceof IActiveDevice) {
//        registrationInfo.setActive(Boolean.TRUE);
//      }
      registrationInfo = registrationRepository.save(registrationInfo);
      log.debug("new device:{} was saved", registrationInfo);
      return null;
    }

    if (!registrationInfo.isRegistered()) {
      return null;
    }

    Sensor sensor = registrationInfo.getSensor();
    SensorDto dto = modelMapper.map(sensor, SensorDto.class);
    log.debug("got configuration {} for device: {} ", dto, registrationInfo);
    return dto;
  }

  private DeviceDto convert(RegistrationInfo registrationInfo) {
    return modelMapper.map(registrationInfo, DeviceDto.class);
  }
}
