package com.group4.DLS.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

import com.group4.DLS.domain.enums.DatasetStatus;

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
    int totalItems;
    LocalDateTime createdAt;
    ProjectResponse project;
    String assignmentId;
    DatasetStatus datasetStatus;

    // List all dataItems
    List<DataItemResponse> dataitems;
    List<LabelResponse> labels;
}
