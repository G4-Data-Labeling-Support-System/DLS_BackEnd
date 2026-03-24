package com.group4.DLS.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum AnnotationStatus {

    NOT_START, // The annotation is null information
    SUBMITTED,// The annotation has been submitted by the annotator and is awaiting review.
    APPROVED,// The annotation has been reviewed and approved by a reviewer or administrator.
    REJECTED,// The annotation has been reviewed and rejected by a reviewer or administrator, possibly with feedback for improvement.
    INACTIVE;

    @JsonCreator
    public static AnnotationStatus fromString(String value) {
        return AnnotationStatus.valueOf(value.toUpperCase());
    }
}
