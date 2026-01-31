package com.group4.DLS.domain.dto.request;

import com.group4.DLS.domain.entity.enums.DatasetStorageType;
import lombok.Getter;

@Getter
public class DatasetUpdateRequest {
    private String datasetName;
    private DatasetStorageType storageType;
}
