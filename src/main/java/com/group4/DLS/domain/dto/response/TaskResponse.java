package com.group4.DLS.domain.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class TaskResponse {
    String taskId;
    String assiognmentId;
    String taskType;
    Integer completedItems;
    Integer totalItems;
    boolean flagForReview;
    String reviewStatus;
    LocalDateTime createdAt;
}
