package com.group4.DLS.domain.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BoundingBoxShape extends Shape {
    Double x;
    Double y;
    Double width;
    Double height;
    Double startX;
    Double startY;
}