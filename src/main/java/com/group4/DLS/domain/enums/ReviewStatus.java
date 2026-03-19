package com.group4.DLS.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ReviewStatus {

    IN_PROGRESS,
    APPROVED, 
    REJECTED,
    INACTIVE;

    @JsonCreator
    public static ReviewStatus fromString(String value) {
        return ReviewStatus.valueOf(value.toUpperCase());
    }
}
