package org.komarichyn.ss.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.komarichyn.ss.api.dto.BaseDto;
import org.komarichyn.ss.api.dto.PagingDto;
import org.komarichyn.ss.api.dto.SensorDataDto;
import org.komarichyn.ss.api.dto.SensorDto;
import org.komarichyn.ss.database.sql.ISensorDataRepository;
import org.komarichyn.ss.database.sql.ISensorRepository;
import org.komarichyn.ss.database.sql.entity.Sensor;
import org.komarichyn.ss.database.sql.entity.SensorData;
import org.komarichyn.ss.service.ISensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SensorService implements ISensorService {

  @Autowired
  private ISensorRepository sensorRepository;
  @Autowired
  private ISensorDataRepository sensorDataRepository;


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
    BaseDto<List<SensorDataDto>> result =  new BaseDto<>();

    Optional<Sensor> optionalSensor = sensorRepository.findById(sensorId);
    if(optionalSensor.isPresent()){
      Page<SensorData> pagedResult = sensorDataRepository.findAllBySensorOrderByCreatedDesc(optionalSensor.get(), paging);
      List<SensorDataDto> r = pagedResult.getContent().stream().map(el -> new SensorDataDto(el.getId(), el.getType(), el.getValue(), el.getCreated())).toList();
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
    Optional<Sensor> s = sensorRepository.findById(id);
    if(s.isPresent()){
      Sensor sensor = s.get();
      SensorDto dto = new SensorDto();
      dto.setName(sensor.getName());
      dto.setOutcome(sensor.getOutcome());
      dto.setIncome(sensor.getIncome());
      dto.setId(sensor.getId());
      result.setContent(dto);
    }
    log.debug("result: {}", result);
    return result;
  }
}
