package com.group4.DLS.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum DataType {
    
    IMAGE,
    TEXT;

    @JsonCreator
    public static DataType fromString(String value) {
        return DataType.valueOf(value.toUpperCase());
    }

}
