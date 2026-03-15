package com.group4.DLS.mappers;

import com.group4.DLS.domain.dto.response.ProjectMemberResponse;
import com.group4.DLS.domain.entity.ProjectMember;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectMemberMapper {


    @Mapping(target = "projectId", source = "project.projectId")
    @Mapping(target = "user", source = "user")
    ProjectMemberResponse toProjectMemberResponse(ProjectMember projectMember);

    List<ProjectMemberResponse> toProjectMemberResponse(List<ProjectMember> projectMembers);
}
