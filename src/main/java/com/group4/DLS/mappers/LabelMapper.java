package com.group4.DLS.mappers;

import com.group4.DLS.domain.dto.request.LabelCreationRequest;
import com.group4.DLS.domain.dto.request.LabelUpdateRequest;
import com.group4.DLS.domain.dto.response.LabelResponse;
import com.group4.DLS.domain.entity.Label;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface LabelMapper {

    // CREATE
    @Mapping(target = "labelId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "annotations", ignore = true)
    @Mapping(target = "dataset", ignore = true)
    Label toLabel(LabelCreationRequest request);

    // RESPONSE
    @Mapping(source = "dataset.datasetId", target = "datasetId")
    LabelResponse toLabelResponse(Label label);

    // UPDATE (chỉ update field != null)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "labelId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "dataset", ignore = true)
    @Mapping(target = "annotations", ignore = true)
    void updateLabel(@MappingTarget Label label, LabelUpdateRequest request);
}