package com.group4.DLS.domain.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class GuidelineResponse {
    String guideId;
    String projectId;
    String guideName;
    String content;
    int version;
    LocalDate createdAt;
    LocalDate updatedAt;
}
