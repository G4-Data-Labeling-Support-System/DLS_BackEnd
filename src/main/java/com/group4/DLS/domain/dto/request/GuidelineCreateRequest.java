package com.group4.DLS.domain.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class GuidelineCreateRequest {
    @Size(max = 50, message = "Title must not exceed 50 characters")
    String title;

    @Size(max = 500, message = "Content must not exceed 500 characters")
    String content;
}

