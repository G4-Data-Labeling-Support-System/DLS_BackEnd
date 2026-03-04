package com.group4.DLS.domain.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ProjectStatus {
    ACTIVE,
    INACTIVE,
    NOT_STARTED,
    IN_PROGRESS,
    COMPLETED,
    ON_HOLD,
    CANCELLED;

    @JsonCreator
    public static ProjectStatus fromString(String value) {
        return ProjectStatus.valueOf(value.toUpperCase());
    }
}
