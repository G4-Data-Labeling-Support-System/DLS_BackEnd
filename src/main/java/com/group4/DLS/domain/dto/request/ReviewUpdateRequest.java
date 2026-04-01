package com.group4.DLS.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ReviewUpdateRequest {
    @NotBlank(message = "TaskId must not be blank")
    String taskId;

    @NotBlank(message = "AnnotationId must not be blank")
    String annotationId;

    @Size(max = 500, message = "Comment must not exceed 500 characters")
    String comment;

    @NotBlank(message = "Review status must not be blank")
    String reviewStatus;

    List<MultipartFile> envidence;
}
