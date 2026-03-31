package com.group4.DLS.domain.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Shape {
    double x;
    double y;
    double width;
    double height;
    String label;

    // optional (không dùng nhưng cần để parse không lỗi)
    String type;
    String color;
    double startX;
    double startY;
}
