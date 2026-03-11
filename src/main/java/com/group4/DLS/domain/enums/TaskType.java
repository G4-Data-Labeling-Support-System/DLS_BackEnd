package com.group4.DLS.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TaskType {
    BATCH,
    ClASSIFICATION;

    @JsonCreator
    public static TaskType fromString(String value) {
        return TaskType.valueOf(value.toUpperCase());
    }
}
