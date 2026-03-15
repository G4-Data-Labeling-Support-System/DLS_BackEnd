package com.group4.DLS.domain.enums;

public enum DatasetStatus {

    ACTIVE,
    INACTIVE;

    public static DatasetStatus fromString(String value) {
        return DatasetStatus.valueOf(value.toUpperCase());
    }
}
