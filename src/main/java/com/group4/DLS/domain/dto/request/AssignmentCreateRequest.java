package com.group4.DLS.domain.dto.request;

import com.group4.DLS.domain.entity.Dataset;
import com.group4.DLS.domain.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
    User assignedTo;

    @NotNull(message = "Assigned By is required")
    User assignedBy;

    String description;

    LocalDateTime dueDate;

    Dataset datasetId;
}