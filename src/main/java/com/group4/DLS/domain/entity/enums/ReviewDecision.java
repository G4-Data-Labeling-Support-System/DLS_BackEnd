package com.group4.DLS.domain.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ReviewDecision {
    APPROVED,
    REJECTED;

    @JsonCreator
    public static ReviewDecision fromString(String value) {
        return ReviewDecision.valueOf(value.toUpperCase());
    }
}
