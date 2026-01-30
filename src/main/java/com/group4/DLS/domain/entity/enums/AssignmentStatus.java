package com.group4.DLS.domain.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum AssignmentStatus {
    OPEN,
    CANCELED,
    CLOSED;

    @JsonCreator
    public static AssignmentStatus fromString(String value) {
        return AssignmentStatus.valueOf(value.toUpperCase());
    }
}
