package com.group4.DLS.mappers;


import com.group4.DLS.domain.dto.response.AnnotationResponse;
import com.group4.DLS.domain.entity.Annotation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AnnotationMapper {

    @Mapping(source = "annotationId", target = "annotationId")
    @Mapping(source = "annotationConfidence", target = "annotationConfidence")
    @Mapping(source = "comment", target = "comment")
    @Mapping(source = "annotationType", target = "annotationType")
    @Mapping(source = "annotationData", target = "annotationData")
    @Mapping(source = "annotationStatus", target = "annotationStatus")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "updatedAt", target = "updatedAt")
    @Mapping(source = "labels", target = "labels")
    @Mapping(source = "reviews", target = "reviews")
    AnnotationResponse toAnnotationResponse(Annotation annotation);

    List<AnnotationResponse> toAnnotationResponses(List<Annotation> annotations);

}
