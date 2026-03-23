package com.group4.DLS.domain.dto.request;

import java.util.List;

import com.group4.DLS.domain.entity.Annotation;

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
public class AnnotationCreationRequest {
    String taskId;
    List<AnnotationItemRequest> annotations;
}
