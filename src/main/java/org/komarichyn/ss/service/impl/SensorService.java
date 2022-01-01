package org.komarichyn.ss.service.impl;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.komarichyn.ss.api.dto.BaseDto;
import org.komarichyn.ss.api.dto.DeviceInfoDto;
import org.komarichyn.ss.api.dto.PagingDto;
import org.komarichyn.ss.api.dto.SensorDataDto;
import org.komarichyn.ss.api.dto.SensorDto;
import org.komarichyn.ss.database.sql.IRegistrationRepository;
import org.komarichyn.ss.database.sql.ISensorDataRepository;
import org.komarichyn.ss.database.sql.ISensorRepository;
import org.komarichyn.ss.database.sql.entity.RegistrationInfo;
import org.komarichyn.ss.database.sql.entity.Sensor;
import org.komarichyn.ss.database.sql.entity.SensorData;
import org.komarichyn.ss.service.ISensorService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SensorService implements ISensorService {

  @Autowired
  private ISensorRepository sensorRepository;
  @Autowired
  private ISensorDataRepository sensorDataRepository;
  @Autowired
  private IRegistrationRepository registrationRepository;
  @Autowired
  private ModelMapper modelMapper;


  @Override
  public BaseDto<List<SensorDto>> list(Pageable paging) {
    log.debug("list sensors by paging:{}", paging);
    Page<Sensor> pagedResult = sensorRepository.findAll(paging);
    BaseDto<List<SensorDto>> result = new BaseDto<>();
    if (pagedResult.hasContent()) {
      List<SensorDto> r = pagedResult.getContent().stream().map(el -> new SensorDto(el.getId(), el.getName(), el.getOutcome(), el.getIncome())).toList();
      PagingDto p = PagingDto.builder().page(pagedResult.getNumber()).size(pagedResult.getSize()).total(pagedResult.getTotalElements()).build();
      result.setPagination(p);
      result.setContent(r);
    }
    log.debug("result: {}", result);
    return result;
  }

  @Override
  public BaseDto<List<SensorDataDto>> listData(Long sensorId, Pageable paging) {
    log.debug("listData by sensorId:{} , page: {}", sensorId, paging);
    BaseDto<List<SensorDataDto>> result = new BaseDto<>();

    Optional<Sensor> optionalSensor = sensorRepository.findById(sensorId);
    if (optionalSensor.isPresent()) {
      Page<SensorData> pagedResult = sensorDataRepository.findAllBySensorOrderByCreatedDesc(optionalSensor.get(), paging);
      List<SensorDataDto> r = pagedResult.getContent().stream().map(el -> new SensorDataDto(el.getId(), el.getType(), el.getValue(), el.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
          .toList();
      PagingDto p = PagingDto.builder().page(pagedResult.getNumber()).size(pagedResult.getSize()).total(pagedResult.getTotalElements()).build();
      result.setPagination(p);
      result.setContent(r);
    }
    log.debug("result: {}", result);
    return result;
  }

  @Override
  public BaseDto<SensorDto> getSensorById(Long id) {
    log.debug("get sensor by id: {} ", id);
    BaseDto<SensorDto> result = new BaseDto<>();
    SensorDto dto = this.getSensor(id);
    if (dto != null) {
      result.setContent(dto);
    }
    log.debug("result: {}", result);
    return result;
  }

  @Override
  public SensorDto getSensor(Long id) {
    Optional<Sensor> s = sensorRepository.findById(id);
    if (s.isPresent()) {
      Sensor sensor = s.get();
      SensorDto dto = new SensorDto();
      dto.setName(sensor.getName());
      dto.setOutcome(sensor.getOutcome());
      dto.setIncome(sensor.getIncome());
      dto.setId(sensor.getId());
      return dto;
    }
    return null;
  }

  @Override
  public SensorDto getSensor(String code) {
    log.debug("get sensor by name: {}", code);
    Sensor s = sensorRepository.findByName(code);
    if (s == null) {
      return null;
    }
    SensorDto result = modelMapper.map(s, SensorDto.class);
    log.debug("result: {}", result);
    return result;
  }

  @Override
  public Sensor findSensor(String code) {
    log.debug("find sensor by code: {}", code);
    Sensor s = sensorRepository.findByName(code);
    if (s == null) {
      return null;
    }
    log.debug("result: {}", s);
    return s;
  }

  @Override
  public SensorDto save(SensorDto sensor) {
    log.debug("save new sensor: {}", sensor);
    Sensor s = modelMapper.map(sensor, Sensor.class);
    sensorRepository.save(s);
    sensor = modelMapper.map(s, SensorDto.class);
    log.debug("result: {}", sensor);
    return sensor;
  }

  @Override
  public BaseDto<List<DeviceInfoDto>> listDevices(Pageable paging) {
    Iterable<RegistrationInfo> regInfo = registrationRepository.findAll();
    List<DeviceInfoDto> deviceDtos = new ArrayList<>();
    regInfo.forEach(ri -> {
      DeviceInfoDto dto = new DeviceInfoDto();
      Sensor s = ri.getSensor();
      dto.setCode(ri.getCode());
      dto.setId(ri.getId());
      dto.setActive(ri.isActive());
      dto.setRegistered(ri.isRegistered());
      dto.setName(s.getName());
      dto.setOutcome(s.getOutcome());
      dto.setIncome(s.getIncome());
      dto.setCreated(ri.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
      deviceDtos.add(dto);
    });
    BaseDto<List<DeviceInfoDto>> result = new BaseDto<>();
    result.setContent(deviceDtos);
    return result;
  }

  @Override
  public BaseDto<DeviceInfoDto> updateDeviceInfo(DeviceInfoDto device) {
    Optional<RegistrationInfo> regInfo = registrationRepository.findById(device.getId());
    BaseDto<DeviceInfoDto> result = new BaseDto<>();
    if (regInfo.isPresent()) {
      RegistrationInfo ri = regInfo.get();
      ri.setRegistered(device.isRegistered());
      ri.setActive(device.isActive());
      Sensor s = ri.getSensor();
      s.setName(device.getName());
      s.setOutcome(device.getOutcome());
      s.setIncome(device.getIncome());
      sensorRepository.save(s);
      registrationRepository.save(ri);
      result.setContent(device);
    } else {
      Map<String, String> errors = new HashMap<>();
      errors.put("ERROR_CODE", "could not find device");
      result.setErrorMap(errors);
    }
    return result;
  }
}
