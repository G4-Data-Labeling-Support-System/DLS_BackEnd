package com.group4.DLS.domain.dto.request;

import com.group4.DLS.domain.entity.Label;
import com.group4.DLS.domain.enums.AnnotationConfidence;
import com.group4.DLS.domain.enums.AnnotationType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
 @Builder
 @NoArgsConstructor
 @AllArgsConstructor
 @FieldDefaults(level = AccessLevel.PRIVATE)
 public class AnnotationSaveRequest {
  String taskId;
  String dataitemId;
  List<String> labelIds;

  AnnotationType annotationType;
  Object annotationData;

  AnnotationConfidence annotationConfidence;
  String comment;
 }
