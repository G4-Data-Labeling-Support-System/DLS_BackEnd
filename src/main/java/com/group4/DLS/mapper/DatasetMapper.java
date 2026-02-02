package com.group4.DLS.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.group4.DLS.domain.dto.request.DatasetCreationRequest;
import com.group4.DLS.domain.dto.request.DatasetUpdateRequest;
import com.group4.DLS.domain.dto.response.DatasetResponse;
import com.group4.DLS.domain.entity.Dataset;
import com.group4.DLS.domain.entity.Project;

@Mapper(componentModel = "spring")
public interface DatasetMapper {

    // ===== ENTITY -> RESPONSE =====
    @Mapping(target = "projectId", source = "project.projectId")
    DatasetResponse toResponse(Dataset dataset);

    // ===== CREATE REQUEST -> ENTITY =====
    @Mapping(target = "datasetId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "project", source = "project")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Dataset toDataset(DatasetCreationRequest request, Project project);

    // ===== UPDATE REQUEST -> ENTITY =====
    @Mapping(target = "datasetId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateDataset(DatasetUpdateRequest request, @MappingTarget Dataset dataset);
}
