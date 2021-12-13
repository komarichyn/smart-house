package org.komarichyn.ss.api.dto;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PagingDto implements Serializable {
  private int page;
  private int size;
  private long total;
}
