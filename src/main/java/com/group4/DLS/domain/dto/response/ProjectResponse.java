package com.group4.DLS.domain.dto.response;

import java.time.LocalDate;

import com.group4.DLS.domain.entity.enums.ProjectStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ProjectResponse {
    String projectId;
    String projectName;
    String description;
    ProjectStatus status;
    LocalDate createdAt;
    LocalDate updatedAt;
}
