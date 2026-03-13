package com.group4.DLS.domain.dto.response;


import com.group4.DLS.domain.enums.TaskDataItemStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class TaskDataITemResponse {
    String dataItemId;
    String taskId;
    int itemIndex;
    DataItemResponse dataItem;
    TaskDataItemStatus taskDataItemStatus;
    LocalDateTime assignedAt;
    LocalDateTime completedAt;
}
