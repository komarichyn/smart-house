package org.komarichyn.ss.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SensorDto {
  private Long id;
  private String name;
  private String outcome;
  private String income;

}
