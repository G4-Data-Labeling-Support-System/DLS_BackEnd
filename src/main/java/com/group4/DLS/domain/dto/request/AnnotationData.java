package com.group4.DLS.domain.dto.request;

import com.group4.DLS.domain.dto.response.Shape;
import lombok.Data;

import java.util.List;

@Data
public class AnnotationData {
    private List<Shape> raw;
}
