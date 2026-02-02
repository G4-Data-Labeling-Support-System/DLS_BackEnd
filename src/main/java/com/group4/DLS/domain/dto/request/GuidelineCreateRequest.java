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
    @Size(max = 50, message = "GUIDELINE_NAME_TOO_LONG")
    String guideName;
    @Size(max = 500, message = "GUIDELINE_CONTENT_TOO_LONG")
    String content;
}

