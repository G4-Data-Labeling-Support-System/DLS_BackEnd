package com.group4.DLS.domain.dto.response;

import java.time.LocalDate;

import com.group4.DLS.domain.entity.enums.DatasetStatus;
import com.group4.DLS.domain.entity.enums.DatasetStorageType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class DatasetResponse {

    String datasetId;
    String datasetName;
    int version;
    DatasetStorageType storageType;
    DatasetStatus status;
    String projectId;
    LocalDate createdAt;
    LocalDate updatedAt;
}
