package com.group4.DLS.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TaskDataItemStatus {
    
    INACTIVE,
    PENDING,
    COMPLETED;

    @JsonCreator
    public static TaskDataItemStatus fromString(String value) {
        return TaskDataItemStatus.valueOf(value.toUpperCase());
    }
}
