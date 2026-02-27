package com.group4.DLS.domain.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum LabelSchemaStatus {

    ACTIVE,
    INACTIVE,
    DELETED;

    @JsonCreator
    public static LabelSchemaStatus fromString(String value) {
        return LabelSchemaStatus.valueOf(value.toUpperCase());
    }
}
