package org.komarichyn.ss.api.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SensorDataDto {
  private Long id;
  private int type;
  private String value;
  private LocalDateTime eventDate;
}
