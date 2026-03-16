package com.group4.DLS.mappers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group4.DLS.domain.dto.request.AnnotationSaveRequest;
import com.group4.DLS.domain.entity.Annotation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AnnotationMapper {

    @Mapping(target = "annotationId", ignore = true)
    @Mapping(target = "task", ignore = true)
    @Mapping(target = "dataitem", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "annotationStatus", ignore = true)
    @Mapping(target = "annotationData", expression = "java(convertToJson(request.getAnnotationData()))")
    Annotation toCreateAnnotation (AnnotationSaveRequest request);



    // ===== UPDATE =====
    @Mapping(target = "annotationId", ignore = true)
    @Mapping(target = "task", ignore = true)
    @Mapping(target = "dataitem", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "annotationStatus", ignore = true)
    @Mapping(target = "annotationData", expression = "java(convertToJson(request.getAnnotationData()))")
    void updateAnnotation(AnnotationSaveRequest request, @MappingTarget Annotation annotation);


    //function to change to jason to save database
    default String convertToJson(Object value) {
        try {
            return new ObjectMapper().writeValueAsString(value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
