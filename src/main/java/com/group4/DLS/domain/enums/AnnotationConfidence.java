package com.group4.DLS.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum AnnotationConfidence {
    
    LOW,
    MEDIUM,
    HIGH;

    @JsonCreator
    public static AnnotationConfidence fromString(String value) {
        return AnnotationConfidence.valueOf(value.toUpperCase());
    }
}
