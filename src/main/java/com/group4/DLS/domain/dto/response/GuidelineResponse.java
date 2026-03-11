package com.group4.DLS.domain.dto.response;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

import com.group4.DLS.domain.enums.GuidelineStatus;
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class GuidelineResponse {
    String guideId;
    String projectId;
    String title;
    String content;
    int version;
    GuidelineStatus status;
    LocalDate createdAt;
    LocalDate updatedAt;
    ProjectResponse project;
}
