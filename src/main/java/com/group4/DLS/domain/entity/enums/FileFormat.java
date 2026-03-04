package com.group4.DLS.domain.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum FileFormat {
    
    PNG,
    JPG,
    JPEG;

    @JsonCreator
    public static FileFormat fromString(String value) {
        return FileFormat.valueOf(value.toUpperCase());
    }

}
