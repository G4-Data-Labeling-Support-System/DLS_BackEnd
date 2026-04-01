package com.group4.DLS.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ProjectUpdateRequest {

    @NotBlank(message = "Project name must not be blank")
    @Size(min = 3, max = 255, message = "Project name must be between 3 and 255 characters")
    String projectName;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    String description;
}
