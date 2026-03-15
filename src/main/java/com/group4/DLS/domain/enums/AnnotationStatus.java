package com.group4.DLS.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum AnnotationStatus {
    DRAFT,// The annotation is being created or edited by the annotator.
    SUBMITTED,// The annotation has been submitted by the annotator and is awaiting review.
    APPROVED,// The annotation has been reviewed and approved by a reviewer or administrator.
    REJECTED,// The annotation has been reviewed and rejected by a reviewer or administrator, possibly with feedback for improvement.
    DELETED;// The annotation has been marked as deleted and is no longer active in the system.

    @JsonCreator
    public static AnnotationStatus fromString(String value) {
        return AnnotationStatus.valueOf(value.toUpperCase());
    }
}
