package com.group4.DLS.domain.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum DatasetStorageType {
    LOCAL,
    CLOUD;

    @JsonCreator
    public static DatasetStorageType fromString(String value) {
        for (DatasetStorageType type : DatasetStorageType.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown DatasetStorageType: " + value);
    }
}
