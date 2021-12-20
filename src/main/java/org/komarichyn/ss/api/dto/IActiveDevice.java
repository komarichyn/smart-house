package org.komarichyn.ss.api.dto;

public interface IActiveDevice extends IDevice{

  default boolean isActive() {
    return Boolean.TRUE;
  };

}
