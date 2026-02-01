package com.group4.DLS.domain.dto.request;

import com.group4.DLS.domain.entity.enums.ProjectStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
public class ProjectCreationRequest {
    @NotBlank(message = "PROJECT_NAME_REQUIRED")
    @Size(min = 3, max = 100, message = "INVALID_PROJECT_NAME_LENGTH")
    String projectName;

    @Size(max = 500, message = "INVALID_PROJECT_DESCRIPTION_LENGTH")
    String description;

    ProjectStatus status;
}
