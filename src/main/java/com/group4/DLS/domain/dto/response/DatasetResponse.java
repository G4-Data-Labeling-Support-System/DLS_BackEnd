package com.group4.DLS.domain.dto.response;

import com.group4.DLS.domain.enums.DataItemStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class DatasetResponse {

    String datasetId;
    String datasetName;
    String description;
    DataItemStatus dataItemStatus;
    int totalItems;
    LocalDateTime createdAt;
    ProjectResponse project;
    String assignmentId;
    // List all dataItems
    List<DataItemResponse> dataitems;
    List<LabelResponse> labels;
}
