package com.group4.DLS.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum LabelStatus {
    
    ACTIVE,
    INACTIVE;

    @JsonCreator
    public static LabelStatus fromString(String value) {
        return LabelStatus.valueOf(value.toUpperCase());
    }
}
