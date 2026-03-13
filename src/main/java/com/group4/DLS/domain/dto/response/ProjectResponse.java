package com.group4.DLS.domain.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.group4.DLS.domain.enums.ProjectStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.type.descriptor.java.LocalDateJavaType;

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
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
