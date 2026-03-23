package com.group4.DLS.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum DatasetStatus {
    
    ACTIVE,
    INACTIVE;

    @JsonCreator
    public static DatasetStatus fromString(String value) {
        return DatasetStatus.valueOf(value.toUpperCase());
    }
}
