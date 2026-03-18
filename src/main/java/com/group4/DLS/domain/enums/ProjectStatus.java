package com.group4.DLS.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ProjectStatus {
    
    NOT_STARTED,
    IN_PROGRESS,
    COMPLETED,
    INACTIVE;

    @JsonCreator
    public static ProjectStatus fromString(String value) {
        return ProjectStatus.valueOf(value.toUpperCase());
    }
}
