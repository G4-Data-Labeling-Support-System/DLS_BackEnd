package com.group4.DLS.domain.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Shape {
        //  phân loại shape
        String type; // "bounding_box" | "polygon"

        // ======================
        // 📦Bounding Box
        // ======================
        Double x;
        Double y;
        Double width;
        Double height;
        Double startX;
        Double startY;

        // ======================
        //  Polygon
        // ======================
//        List<List<Double>> points;

        // ======================
        //  Common
        // ======================
        String label;
        String color;
}

