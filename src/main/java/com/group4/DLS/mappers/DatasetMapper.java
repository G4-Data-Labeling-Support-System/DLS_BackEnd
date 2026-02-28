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

    @Mapping(target = "assignmentId", source = "assignment.assignmentId")
    DatasetResponse toDatasetResponse(Dataset dataset);

    @Mapping(target = "assignmentId", source = "assignment.assignmentId")
    List<DatasetResponse> toDatasetResponse(List<Dataset> datasets);

    @Mapping(target = "datasetId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "dataitems", ignore = true)
    Dataset createDatasetFromRequest(
        DatasetCreationRequest request
    );

    @Mapping(target = "datasetId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "dataitems", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateDatasetFromRequest(
        DatasetUpdateRequest request,
        @MappingTarget Dataset dataset
    );
}
