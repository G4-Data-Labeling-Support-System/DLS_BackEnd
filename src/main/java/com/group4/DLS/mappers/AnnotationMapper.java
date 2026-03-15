package com.group4.DLS.mappers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group4.DLS.domain.dto.request.AnnotationSaveRequest;
import com.group4.DLS.domain.entity.Annotation;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class AnnotationMapper {

    @Autowired
    protected ObjectMapper objectMapper;

    @Mapping(target = "annotationId", ignore = true)
    @Mapping(target = "task", ignore = true)
    @Mapping(target = "dataitem", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "annotationStatus", ignore = true)
    public abstract Annotation toEntity(AnnotationSaveRequest request);

    public String map(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (Exception e) {
            throw new RuntimeException("Error converting annotationData to JSON", e);
        }
    }
}