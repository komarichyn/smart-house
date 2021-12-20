package org.komarichyn.ss.api.dto;

public interface IPassiveDevice extends IDevice {
    default boolean isActive() {
        return Boolean.FALSE;
    };
}
