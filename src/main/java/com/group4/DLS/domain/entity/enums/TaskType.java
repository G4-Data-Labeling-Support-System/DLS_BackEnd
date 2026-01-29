package com.group4.DLS.domain.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TaskType {
    ClASSIFICATION;

    @JsonCreator
    public static TaskType fromString(String value) {
        return TaskType.valueOf(value.toUpperCase());
    }
}
