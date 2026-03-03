package com.group4.DLS.domain.dto.response;

import com.group4.DLS.domain.entity.enums.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

import com.group4.DLS.domain.entity.enums.AssignmentStatus;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class AssignmentResponse {
    String assignmentId;
    String assignmentName;
    String description;
    AssignmentStatus assignmentStatus;
    String assignedBy;
    String assignedTo;
    String projectId;
    String datasetId;
    LocalDate createdAt;
    LocalDate updatedAt;
    ProjectResponse project;
    DatasetResponse dataset;
}
