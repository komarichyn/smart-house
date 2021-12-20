package org.komarichyn.ss.service;

import org.komarichyn.ss.api.dto.IDevice;
import org.komarichyn.ss.api.dto.SensorDto;

public interface IDeviceRegistrationService {

  public boolean isActivated(IDevice device);
  public IDevice activate(IDevice device);
  public IDevice deactivate(IDevice device);
  public SensorDto registration(IDevice device);

}
