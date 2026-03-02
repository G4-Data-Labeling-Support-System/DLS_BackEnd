package com.group4.DLS.domain.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum AnnotationType {
    
    CLASSIFICATION,
    BOUNDING_BOX,
    POLYGON,
    SEGMENTATION;

    @JsonCreator
    public static AnnotationType fromString(String value) {
        return AnnotationType.valueOf(value.toUpperCase());
    }

}
