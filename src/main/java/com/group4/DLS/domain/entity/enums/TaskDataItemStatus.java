package com.group4.DLS.domain.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TaskDataItemStatus {
    
    PENDING,
    COMPLETED;

    @JsonCreator
    public static TaskDataItemStatus fromString(String value) {
        return TaskDataItemStatus.valueOf(value.toUpperCase());
    }
}
