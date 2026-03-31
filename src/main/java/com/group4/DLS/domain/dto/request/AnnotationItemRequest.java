package com.group4.DLS.domain.dto.request;

import java.util.List;

import com.group4.DLS.domain.dto.response.LabelResponse;
import com.group4.DLS.domain.enums.AnnotationConfidence;
import com.group4.DLS.domain.enums.AnnotationStatus;
import com.group4.DLS.domain.enums.AnnotationType;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @NotBlank(message = "Task ID must not be blank")
    String taskId;

    @NotNull(message = "Annotation confidence is required")
    AnnotationConfidence annotationConfidence;

    @NotNull(message = "Annotation data is required")
    @Valid
    AnnotationData annotationData;

    @NotNull(message = "Annotation status is required")
    AnnotationStatus annotationStatus;

    @NotNull(message = "Annotation type is required")
    AnnotationType annotationType;

    @Size(max = 500, message = "Comment must not exceed 500 characters")
    String comment;

    @NotBlank(message = "Data item ID must not be blank")
    String dataitemId;

    @NotEmpty(message = "At least one label is required")
    List<@NotBlank(message = "Label ID must not be blank") String> labelIds;
}
