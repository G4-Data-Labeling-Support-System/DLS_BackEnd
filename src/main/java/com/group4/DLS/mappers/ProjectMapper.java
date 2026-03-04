package com.group4.DLS.mappers;


import java.util.List;

import org.mapstruct.*;

import com.group4.DLS.domain.dto.request.ProjectCreationRequest;
import com.group4.DLS.domain.dto.request.ProjectStatusUpdateRequest;
import com.group4.DLS.domain.dto.request.ProjectUpdateRequest;
import com.group4.DLS.domain.dto.response.ProjectResponse;
import com.group4.DLS.domain.entity.Project;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    ProjectResponse toProjectResponse(Project project);

    List<ProjectResponse> toProjectResponse(List<Project> projects);

    @Mapping(target = "projectId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "assignments", ignore = true)
    @Mapping(target = "guidelines", ignore = true)
    @Mapping(target = "datasets", ignore = true)
    Project createProjectFromRequest(
        ProjectCreationRequest request
    );

    @Mapping(target = "projectId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "assignments", ignore = true)
    @Mapping(target = "guidelines", ignore = true)
    @Mapping(target = "datasets", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProjectFromRequest(
        ProjectUpdateRequest request, 
        @MappingTarget Project project
    );

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProjectStatusFromRequest(
        ProjectStatusUpdateRequest request, 
        @MappingTarget Project project
    );
}
