package org.komarichyn.ss.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.komarichyn.ss.api.dto.DeviceDto;
import org.komarichyn.ss.api.dto.IDevice;
import org.komarichyn.ss.api.dto.SensorDto;

public interface IDeviceService {

  IDevice parseJsonToDevice(String json) throws JsonProcessingException ;

  String toJsonFromDevice(IDevice device) throws JsonProcessingException;
}
