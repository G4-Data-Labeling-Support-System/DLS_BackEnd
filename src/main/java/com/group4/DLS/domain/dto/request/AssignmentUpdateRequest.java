package com.group4.DLS.domain.dto.request;

import com.group4.DLS.domain.entity.User;
import com.group4.DLS.domain.entity.enums.AssignmentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AssignmentUpdateRequest {

    @NotBlank(message = "Assignment name is required")
    String assignmentName;

    @NotNull(message = "Assigned To is required")
    User assignedTo;

    String description;

    LocalDateTime dueDate;

    AssignmentStatus assignmentStatus;
}