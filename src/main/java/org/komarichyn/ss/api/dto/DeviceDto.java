package org.komarichyn.ss.api.dto;

import lombok.Data;

@Data
public class DeviceDto implements IPassiveDevice, IActiveDevice{
  private Long id;
  private String code;
  private SensorDto sensor;
  private boolean active;
  private String created;
  private boolean registered;
}
