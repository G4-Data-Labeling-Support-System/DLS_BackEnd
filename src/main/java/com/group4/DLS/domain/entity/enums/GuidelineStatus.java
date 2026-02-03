package com.group4.DLS.domain.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum GuidelineStatus {
    ACTIVE,
    INACTIVE;

    @JsonCreator
    public static GuidelineStatus fromString(String value) {
        return GuidelineStatus.valueOf(value.toUpperCase());
    }

}
