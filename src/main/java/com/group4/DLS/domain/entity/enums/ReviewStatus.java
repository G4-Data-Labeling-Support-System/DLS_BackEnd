package com.group4.DLS.domain.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ReviewStatus {

    PENDING,
    APPROVED,
    CORRECTED,
    REJECTED;

    @JsonCreator
    public static ReviewStatus fromString(String value) {
        return ReviewStatus.valueOf(value.toUpperCase());
    }
}
