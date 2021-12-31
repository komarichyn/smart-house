package org.komarichyn.ss.api.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class DeviceDto implements IPassiveDevice, IActiveDevice{
  private Long id;
  private String code;
  private SensorDto sensor;
  private boolean active;
  private LocalDateTime created;
  private boolean registered;
}
