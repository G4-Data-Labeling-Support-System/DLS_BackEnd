package com.group4.DLS.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ReviewStatus {

    APPROVED,
    CORRECTED,
    REJECTED;

    @JsonCreator
    public static ReviewStatus fromString(String value) {
        return ReviewStatus.valueOf(value.toUpperCase());
    }
}
