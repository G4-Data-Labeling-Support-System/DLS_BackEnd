package com.group4.DLS.domain.dto.response;

import com.group4.DLS.domain.entity.enums.AssignmentStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class AssignmentResponse {
    String assignmentId;
    String assignmentName;
    String descriptionAssignment;
    AssignmentStatus assignmentStatus;
    String projectId;
    String datasetId;
    LocalDate createdAt;
    LocalDate updatedAt;
}
