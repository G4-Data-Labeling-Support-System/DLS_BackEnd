package com.group4.DLS.mappers;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.group4.DLS.domain.dto.request.DatasetCreationRequest;
import com.group4.DLS.domain.dto.request.DatasetUpdateRequest;
import com.group4.DLS.domain.dto.response.DatasetResponse;
import com.group4.DLS.domain.entity.Dataset;

@Mapper(componentModel = "spring")
public interface DatasetMapper {

    @Mapping(target = "project", source = "project")
    @Mapping(target = "assignmentId", source = "assignment.assignmentId") // Get labels from project
    DatasetResponse toDatasetResponse(Dataset dataset);

    List<DatasetResponse> toDatasetResponse(List<Dataset> datasets);

    @Mapping(target = "datasetId", ignore = true) // Auto generated
    @Mapping(target = "datasetName", source = "datasetName")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "totalItems", ignore = true) // Set default = 0
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "dataitems", ignore = true) // No data item available yet 
    @Mapping(target = "project", ignore = true) // Will set inside service
    @Mapping(target = "labels", ignore = true) // No label available yet
    Dataset createDatasetFromRequest(
        DatasetCreationRequest request
    );

    @Mapping(target = "datasetId", ignore = true)
    @Mapping(target = "datasetName", ignore = false)
    @Mapping(target = "description", ignore = false)
    @Mapping(target = "totalItems", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "dataitems", ignore = true)
    @Mapping(target = "project", ignore = true)
    @Mapping(target = "labels", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateDatasetFromRequest(
        DatasetUpdateRequest request,
        @MappingTarget Dataset dataset
    );
}
