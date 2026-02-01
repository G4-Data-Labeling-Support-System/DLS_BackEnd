package com.group4.DLS.mapper;

import com.group4.DLS.domain.dto.request.AssignmentCreateRequest;
import com.group4.DLS.domain.dto.response.AssignmentResponse;
import com.group4.DLS.domain.entity.Assignment;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AssignmentMapper {

    // ===== CREATE =====
    @Mapping(target = "assignmentId", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "dataset", ignore = true)
    @Mapping(target = "datasets", ignore = true)
    @Mapping(target = "tasks", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Assignment toAssignment(AssignmentCreateRequest request);

    // ===== RESPONSE =====
    @Mapping(target = "projectId", source = "project.projectId")
    @Mapping(target = "datasetId", source = "dataset.datasetId")
    AssignmentResponse toResponse(Assignment assignment);

    List<AssignmentResponse> toResponse(List<Assignment> assignments);
}
