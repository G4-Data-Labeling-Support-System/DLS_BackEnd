package com.group4.DLS.mapper;


import com.group4.DLS.domain.dto.request.ProjectCreationRequest;
import com.group4.DLS.domain.dto.request.ProjectUpdateRequest;
import com.group4.DLS.domain.dto.response.ProjectResponse;
import com.group4.DLS.domain.entity.Project;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    ProjectResponse toProjectResponse(Project project);

    @Mapping(target = "projectId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "assignments", ignore = true)
    @Mapping(target = "guidelines", ignore = true)
    @Mapping(target = "datasets", ignore = true)
    @Mapping(target = "dataitems", ignore = true)
    @Mapping(target = "schemas", ignore = true)

    Project toProject(ProjectCreationRequest request);

    @Mapping(target = "projectId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "assignments", ignore = true)
    @Mapping(target = "guidelines", ignore = true)
    @Mapping(target = "datasets", ignore = true)
    @Mapping(target = "dataitems", ignore = true)
    @Mapping(target = "schemas", ignore = true)

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProjectFromRequest(ProjectUpdateRequest request,
                                  @MappingTarget Project project);
}
