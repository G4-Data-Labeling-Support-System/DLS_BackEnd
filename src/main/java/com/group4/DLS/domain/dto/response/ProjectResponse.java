package com.group4.DLS.domain.dto.response;

import com.group4.DLS.domain.entity.enums.ProjectStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ProjectResponse {
    private String projectId;
    private String projectName;
    private String description;
    private ProjectStatus status;
    private LocalDate createdAt;
}
