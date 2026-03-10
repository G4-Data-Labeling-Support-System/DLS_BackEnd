package com.group4.DLS.domain.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class TaskReponse {
    String taskId;
    String assiognmentId;
    String taskType;
    Integer completedItems;
    Integer totalItems;
    boolean flagForReview;
    String reviewStatus;
}
