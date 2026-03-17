package com.group4.DLS.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AssignmentCreateRequest {

    @NotBlank(message = "Assignment name is required")
    String assignmentName;

    @NotNull(message = "Assigned To is required")
    String assignedTo;

    @NotNull(message = "Assigned By is required")
    String assignedBy;

    @NotNull(message = "Reviewer is required")
    String reviewedBy;

    String description;

    LocalDateTime dueDate;

    @NotBlank(message = "Dataset is required")
    String datasetId;
}