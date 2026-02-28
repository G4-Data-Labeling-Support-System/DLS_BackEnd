package com.group4.DLS.domain.dto.request;

import com.group4.DLS.domain.entity.enums.DatasetStorageType;

import jakarta.validation.constraints.Size;
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
public class DatasetCreationRequest {
    @Size(min = 3, max = 100, message = "INVALID_DATASET_NAME_LENGTH")
    String datasetName;

    int version;
    DatasetStorageType storageType;
    String projectId;
}
