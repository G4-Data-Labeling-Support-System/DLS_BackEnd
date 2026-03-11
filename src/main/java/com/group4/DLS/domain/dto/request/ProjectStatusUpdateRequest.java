package com.group4.DLS.domain.dto.request;

import com.group4.DLS.domain.enums.ProjectStatus;

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
public class ProjectStatusUpdateRequest {
    ProjectStatus status;
}
