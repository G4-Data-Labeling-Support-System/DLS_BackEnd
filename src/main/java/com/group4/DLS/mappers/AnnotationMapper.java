package com.group4.DLS.mappers;

import com.group4.DLS.domain.dto.request.AnnotationItemRequest;
import com.group4.DLS.domain.dto.response.AnnotationResponse;
import com.group4.DLS.domain.entity.Annotation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AnnotationMapper {

    AnnotationResponse toAnnotationResponse(Annotation annotation);

    List<AnnotationResponse> toAnnotationResponses(List<Annotation> annotations);

}
