package com.group4.DLS.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group4.DLS.domain.dto.request.AnnotationData;
import jakarta.persistence.AttributeConverter;
import org.springframework.stereotype.Component;

@Component
public class AnnotationDataConverter implements AttributeConverter<AnnotationData, String> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(AnnotationData attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AnnotationData convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, AnnotationData.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
