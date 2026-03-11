package com.group4.DLS.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum TaskStatus {
    NOT_STARTED,// Default status when a task is created
    IN_PROGRESS,// Indicates that the task is currently being worked on
    COMPLETED,// Indicates that the task has been finished successfully
    ON_HOLD,// Indicates that the task is temporarily paused and not currently being worked on
    CANCELLED;// Indicates that the task has been cancelled and will not be completed

    @JsonCreator
    public static TaskStatus fromString(String value) {
        return TaskStatus.valueOf(value.toUpperCase());
    }
}
