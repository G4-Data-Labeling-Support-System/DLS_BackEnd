package com.group4.DLS.domain.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum UserStatus {
    ACTIVE,
    INACTIVE,
    SUSPENDED;

    @JsonCreator
    public static UserStatus fromString(String value) {
        return UserStatus.valueOf(value.toUpperCase());
    }
}
