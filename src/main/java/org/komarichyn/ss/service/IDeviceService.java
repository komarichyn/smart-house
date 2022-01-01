package org.komarichyn.ss.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import org.komarichyn.ss.api.dto.BaseDto;
import org.komarichyn.ss.api.dto.DeviceDto;
import org.komarichyn.ss.api.dto.IDevice;
import org.komarichyn.ss.api.dto.SensorDto;
import org.springframework.data.domain.Pageable;

public interface IDeviceService {

  IDevice parseJsonToDevice(String json) throws JsonProcessingException ;

  String toJsonFromDevice(IDevice device) throws JsonProcessingException;

  Map<String, String> parseJsonToDeviceData(String json) throws JsonProcessingException;
}
