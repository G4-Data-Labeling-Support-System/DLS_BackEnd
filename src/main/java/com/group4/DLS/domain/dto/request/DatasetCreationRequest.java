package com.group4.DLS.domain.dto.request;

import com.group4.DLS.domain.entity.enums.DatasetStorageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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

    @NotBlank(message = "DATASET_NAME_REQUIRED")
    @Size(min = 3, max = 100, message = "INVALID_DATASET_NAME_LENGTH")
    String datasetName;
    @Builder.Default
    @Positive(message = "INVALID_DATASET_VERSION")
    int version = 1;

    @NotNull(message = "DATASET_STORAGE_TYPE_REQUIRED")
    DatasetStorageType storageType;

    @NotBlank(message = "PROJECT_ID_REQUIRED")
    String projectId;
}
