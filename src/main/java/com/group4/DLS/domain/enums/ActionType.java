package com.group4.DLS.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ActionType {

    CREATE,
    UPDATE,
    DELETE,
    LOGIN,
    LOGOUT,
    VIEW,
    SUBMIT;

    @JsonCreator
    public static ActionType fromString(String value) {
        return ActionType.valueOf(value.toUpperCase());
    }
}
