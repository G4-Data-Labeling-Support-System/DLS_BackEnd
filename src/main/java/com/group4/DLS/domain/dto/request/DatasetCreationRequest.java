package com.group4.DLS.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "REQUIRE_PROJECT_ID")
    String projectId;

    @NotBlank(message = "DATASETNAME_CANNOT_BE_NULL")
    @Size(min = 3, max = 255, message = "INVALID_DATASET_NAME_LENGTH")
    String datasetName;
    
    @Size(max = 1000, message = "INVALID_DATASET_DESCRIPTION_LENGTH")
    String description;

}
