package com.group4.DLS.mappers;

import com.group4.DLS.domain.dto.response.ProjectResponse;
import com.group4.DLS.domain.entity.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.group4.DLS.domain.dto.request.GuidelineCreateRequest;
import com.group4.DLS.domain.dto.response.GuidelineResponse;
import com.group4.DLS.domain.entity.Guideline;

@Mapper(componentModel = "spring")
public interface GuidelineMapper {

    /* =========================
     * Entity → Response
     * ========================= */
    @Mapping(target = "projectId", source = "project.projectId")
    @Mapping(target = "project", source = "project")
    GuidelineResponse toResponse(Guideline guideline);

    /* =========================
     * Request → Entity (Create)
     * ========================= */
    @Mapping(target = "guideId", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Guideline toEntity(GuidelineCreateRequest request);
}
