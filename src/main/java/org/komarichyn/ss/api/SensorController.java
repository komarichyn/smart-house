package org.komarichyn.ss.api;

import java.util.List;
import org.komarichyn.ss.api.dto.BaseDto;
import org.komarichyn.ss.api.dto.SensorDataDto;
import org.komarichyn.ss.api.dto.SensorDto;
import org.komarichyn.ss.service.ISensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "/api")
public class SensorController {

    @Autowired
    private ISensorService sensorService;

    @GetMapping(value = "/sensors/list")
    public ResponseEntity<BaseDto<List<SensorDto>>> getSensors(
        @RequestParam(defaultValue = "0") Integer pageNo,
        @RequestParam(defaultValue = "10") Integer pageSize,
        @RequestParam(defaultValue = "id") String sortBy){
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        return ResponseEntity.ok(sensorService.list(paging));
    }

    @GetMapping(value = "/sensors/{id}")
    public ResponseEntity<BaseDto<SensorDto>> getSensor(@PathVariable Long id){
        return ResponseEntity.ok(sensorService.getSensorById(id));
    }


    @GetMapping(value = "/sensors/listData/{sensorId}")
    public ResponseEntity<BaseDto<List<SensorDataDto>>> getSensorData(
        @PathVariable Long sensorId,
        @RequestParam(defaultValue = "0") Integer pageNo,
        @RequestParam(defaultValue = "10") Integer pageSize,
        @RequestParam(defaultValue = "id") String sortBy){
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

        return ResponseEntity.ok(sensorService.listData(sensorId, paging));
    }
}
