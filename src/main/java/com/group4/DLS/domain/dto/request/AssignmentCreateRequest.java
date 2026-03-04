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
public class AssignmentCreateRequest {
    @Size(max = 100, message = "ASSIGNMENT_NAME_TOO_LONG")
    String assignmentName;
    @Size(max = 500, message = "ASSIGNMENT_DESCRIPTION_TOO_LONG")
    String descriptionAssignment;
}
