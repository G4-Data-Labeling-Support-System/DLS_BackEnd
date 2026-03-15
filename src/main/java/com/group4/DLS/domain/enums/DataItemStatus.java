package com.group4.DLS.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum DataItemStatus {
    ACTIVE,
    INACTIVE,
    DELETED;


    @JsonCreator
    public static DataItemStatus fromString(String value) {
        return DataItemStatus.valueOf(value.toUpperCase());
    }
}
