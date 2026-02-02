package com.group4.DLS.domain.dto.request;

import com.group4.DLS.domain.entity.enums.DatasetStorageType;
import lombok.Getter;

@Getter
public class DatasetCreationRequest {
    private String datasetName;
    private int version;
    private DatasetStorageType storageType;
    private String projectId;
}
