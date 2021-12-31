package org.komarichyn.ss.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.komarichyn.ss.api.dto.BaseDto;
import org.komarichyn.ss.api.dto.DeviceDto;
import org.komarichyn.ss.api.dto.IDevice;
import org.komarichyn.ss.api.dto.SensorDto;
import org.komarichyn.ss.service.IDeviceService;
import org.komarichyn.ss.service.ISensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DeviceService implements IDeviceService {

  @Autowired
  private ISensorService sensorService;
  @Autowired
  private ObjectMapper objectMapper;

  @Override
  public IDevice parseJsonToDevice(String json) throws JsonProcessingException {
    IDevice device = objectMapper.readValue(json, DeviceDto.class);
    SensorDto sensorDto = sensorService.getSensor(device.getCode());
    device.setSensor(sensorDto);
    return device;
  }

  @Override
  public String toJsonFromDevice(IDevice device) throws JsonProcessingException {
    log.debug("transform device {} to json", device);
    String json = objectMapper.writeValueAsString(device);
    log.debug("result: {}", json);
    return json;
  }
}
