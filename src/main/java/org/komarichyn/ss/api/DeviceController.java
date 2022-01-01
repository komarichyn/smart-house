package org.komarichyn.ss.api;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.komarichyn.ss.api.dto.BaseDto;
import org.komarichyn.ss.api.dto.DeviceDto;
import org.komarichyn.ss.api.dto.DeviceInfoDto;
import org.komarichyn.ss.api.dto.SensorDto;
import org.komarichyn.ss.service.IDeviceService;
import org.komarichyn.ss.service.ISensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class DeviceController {

  @Autowired
  private ISensorService sensorService;

  @GetMapping(value = "/devices/list")
  public ResponseEntity<BaseDto<List<DeviceInfoDto>>> getDevices(
      @RequestParam(defaultValue = "0") Integer pageNo,
      @RequestParam(defaultValue = "10") Integer pageSize,
      @RequestParam(defaultValue = "id") String sortBy){
    Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
    return ResponseEntity.ok(sensorService.listDevices(paging));
  }

  @PutMapping(value = "/devices/update")
  public ResponseEntity<BaseDto<DeviceInfoDto>> updateDevice(
      @RequestBody DeviceInfoDto deviceDto){
    BaseDto<DeviceInfoDto> result = sensorService.updateDeviceInfo(deviceDto);
    log.debug("device was updated");
    return ResponseEntity.ok(result);
  }

}
