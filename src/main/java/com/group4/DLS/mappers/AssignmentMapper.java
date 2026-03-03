package com.group4.DLS.mappers;

import org.mapstruct.*;

import com.group4.DLS.domain.dto.request.AssignmentCreateRequest;
import com.group4.DLS.domain.dto.response.AssignmentResponse;
import com.group4.DLS.domain.entity.Assignment;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AssignmentMapper {

    // ===== CREATE =====
    @Mapping(target = "assignmentId", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "dataset", ignore = true)
    @Mapping(target = "assignmentStatus", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "assignedBy", ignore = true)
    @Mapping(target = "assignedTo", ignore = true)
    Assignment toAssignment(AssignmentCreateRequest request);

    // ===== RESPONSE =====
    @Mapping(target = "projectId", source = "project.projectId")
    @Mapping(target = "datasetId", source = "dataset.datasetId")
    @Mapping(target = "assignedTo", source = "assignedTo.userId")
    @Mapping(target = "assignedBy", source = "assignedBy.userId")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "project", source = "project")
    @Mapping(target = "dataset", source = "dataset")
    AssignmentResponse toResponse(Assignment assignment);

    @Mapping(target = "assignmentId", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "dataset", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Assignment updateAssignmentFromRequest(Assignment assignment);
}
