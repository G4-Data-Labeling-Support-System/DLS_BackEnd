package com.group4.DLS.domain.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.group4.DLS.domain.dto.request.AnnotationData;
import com.group4.DLS.domain.enums.AnnotationConfidence;
import com.group4.DLS.domain.enums.AnnotationStatus;
import com.group4.DLS.domain.enums.AnnotationType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class AnnotationResponse {
    String annotationId;
    AnnotationConfidence annotationConfidence;
    String comment;
    AnnotationType annotationType;
    AnnotationData annotationData;
    AnnotationStatus annotationStatus;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    // Lists
    List<LabelResponse> labels;
    List<ReviewResponse> reviews;
}
