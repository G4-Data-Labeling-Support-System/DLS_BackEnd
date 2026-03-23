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

    // ===== CREATE MAPPER =====
    @Mapping(target = "annotationId", ignore = true)
    @Mapping(target = "task", ignore = true)
    @Mapping(target = "dataitem", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "annotationStatus", ignore = true)
    @Mapping(target = "annotationData", ignore = true)
    Annotation toCreateAnnotationRequest(AnnotationItemRequest request);

    // ===== UPDATE MAPPER =====
    @Mapping(target = "annotationId", ignore = true)
    @Mapping(target = "task", ignore = true)
    @Mapping(target = "dataitem", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "annotationStatus", ignore = true)
    @Mapping(target = "annotationData", ignore = true)
    void updateAnnotation(AnnotationItemRequest request, @MappingTarget Annotation annotation);

}
