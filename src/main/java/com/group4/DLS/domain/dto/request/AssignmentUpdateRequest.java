package com.group4.DLS.domain.dto.request;

import com.group4.DLS.domain.entity.enums.AssignmentStatus;
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

    String assignmentName;

    String description;

    LocalDateTime dueDate;

    String assignmentStatus;
}