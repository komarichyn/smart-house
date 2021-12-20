package org.komarichyn.ss.api.dto;

import java.io.Serializable;

public interface IDevice extends Serializable {
  String getCode();
  SensorDto getSensor();
}
