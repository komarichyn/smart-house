package org.komarichyn.ss.service;

import java.util.List;
import org.komarichyn.ss.api.dto.BaseDto;
import org.komarichyn.ss.api.dto.DeviceDto;
import org.komarichyn.ss.api.dto.DeviceInfoDto;
import org.komarichyn.ss.api.dto.SensorDataDto;
import org.komarichyn.ss.api.dto.SensorDto;
import org.springframework.data.domain.Pageable;

public interface ISensorService {

  BaseDto<List<SensorDto>> list(Pageable paging);
  BaseDto<List<SensorDataDto>> listData(Long sensorId, Pageable paging);

  BaseDto<SensorDto> getSensorById(Long id);

  SensorDto getSensor(Long id);

  SensorDto getSensor(String code);

  SensorDto save(SensorDto sensor);

  BaseDto<List<DeviceInfoDto>> listDevices(Pageable paging);
}
