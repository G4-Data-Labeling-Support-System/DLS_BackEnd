package com.group4.DLS.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum UserRole {
    ADMIN,
    MANAGER,
    ANNOTATOR,
    REVIEWER;

    @JsonCreator
    public static UserRole fromString(String value) {
        return UserRole.valueOf(value.toUpperCase());
    }
}
