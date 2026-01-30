package com.group4.DLS.domain.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum AnnotationStatus {
    DRAFT,
    SUBMITTED,
    APPROVED,
    REJECTED;

    @JsonCreator
    public static AnnotationStatus fromString(String value) {
        return AnnotationStatus.valueOf(value.toUpperCase());
    }
}
