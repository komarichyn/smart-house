package org.komarichyn.ss.service;

import org.komarichyn.ss.api.dto.IDevice;
import org.komarichyn.ss.api.dto.SensorDto;

public interface IDeviceRegistrationService {

  boolean isActivated(IDevice device);

  IDevice activate(IDevice device);

  IDevice deactivate(IDevice device);

  SensorDto registration(IDevice device);

}
