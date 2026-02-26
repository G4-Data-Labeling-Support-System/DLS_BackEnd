package com.group4.DLS.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

import com.group4.DLS.domain.entity.Dataitem;
import com.group4.DLS.domain.entity.enums.DatasetStorageType;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class DatasetResponse {
    String datasetId;
    String assignmentId;
    String datasetName;
    int version;
    DatasetStorageType storageType;
    LocalDate createdAt;
    LocalDate updatedAt;
    List<Dataitem> dataitems;
}
