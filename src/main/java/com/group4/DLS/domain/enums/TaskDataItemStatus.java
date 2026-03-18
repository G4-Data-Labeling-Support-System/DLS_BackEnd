package com.group4.DLS.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TaskDataItemStatus {
    
    IN_PROGRESS,
    COMPLETED,
    INACTIVE;

    @JsonCreator
    public static TaskDataItemStatus fromString(String value) {
        return TaskDataItemStatus.valueOf(value.toUpperCase());
    }
}
