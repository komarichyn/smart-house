package org.komarichyn.ss.api.dto;

import lombok.Data;

@Data
public class DeviceInfoDto {
  private Long id;
  private String code;
  private boolean active;
  private String created;
  private boolean registered;
  private String name;
  private String outcome;
  private String income;
}
