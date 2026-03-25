package com.group4.DLS.domain.dto.request;

import java.util.List;

import com.group4.DLS.domain.dto.response.LabelResponse;
import com.group4.DLS.domain.enums.AnnotationConfidence;
import com.group4.DLS.domain.enums.AnnotationStatus;
import com.group4.DLS.domain.enums.AnnotationType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AnnotationItemRequest {
    String taskId;
    AnnotationConfidence annotationConfidence;
    Object annotationData;
    AnnotationStatus annotationStatus;
    AnnotationType annotationType;
    String comment;
    String dataitemId;
    List<String> labelIds;
}
