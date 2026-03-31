package com.group4.DLS.domain.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @Size(min = 3, max = 255, message = "Assignment name must be between 3 and 255 characters")
    String assignmentName;

    @NotBlank(message = "Assigned To is required")
    String assignedTo;

    @NotBlank(message = "Assigned By is required")
    String assignedBy;

    @NotBlank(message = "Reviewer is required")
    String reviewedBy;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    String description;

    @NotNull(message = "Due date is required")
    @Future(message = "Due date must be in the future")
    LocalDateTime dueDate;

    @NotBlank(message = "Dataset is required")
    String datasetId;
}