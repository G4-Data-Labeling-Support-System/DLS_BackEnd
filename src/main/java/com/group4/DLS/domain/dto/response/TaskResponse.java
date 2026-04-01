package com.group4.DLS.domain.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

import com.group4.DLS.domain.enums.TaskStatus;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class TaskResponse {
    String taskId;
    String assignmentId;
    String taskName;
    String taskType;
    Integer completedItems;
    boolean flagForReview;
    TaskStatus taskStatus;
    LocalDateTime createdAt;
}
