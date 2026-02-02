package com.group4.DLS.domain.dto.request;

import com.group4.DLS.domain.entity.enums.ProjectStatus;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ProjectUpdateRequest {

    String projectName;
    String description;
    ProjectStatus status;
}
