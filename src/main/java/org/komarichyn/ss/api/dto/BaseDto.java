package org.komarichyn.ss.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import org.springframework.data.domain.Pageable;

@Data
@JsonInclude(Include.NON_NULL)
public class BaseDto<T> {
  private Map<String, String> errorMap = new HashMap<>();
  private T content;
  private PagingDto pagination;
}
