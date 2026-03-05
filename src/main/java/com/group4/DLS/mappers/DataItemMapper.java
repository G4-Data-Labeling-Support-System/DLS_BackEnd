package com.group4.DLS.mappers;

import com.group4.DLS.domain.dto.response.DataItemResponse;
import com.group4.DLS.domain.dto.response.DatasetResponse;
import com.group4.DLS.domain.entity.Dataitem;
import com.group4.DLS.domain.entity.Dataset;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DataItemMapper {

    @Mapping(target = "datasetId", source = "dataset.datasetId")
    DataItemResponse toDataItemResponse(Dataitem dataitem);

    List<DataItemResponse> toDataItemResponse(List<Dataitem> dataitems);

}
