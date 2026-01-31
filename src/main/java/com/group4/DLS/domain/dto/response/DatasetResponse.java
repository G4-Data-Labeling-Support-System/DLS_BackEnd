package com.group4.DLS.domain.dto.response;

import com.group4.DLS.domain.entity.enums.DatasetStorageType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class DatasetResponse {
    private String datasetId;
    private String datasetName;
    private int version;
    private DatasetStorageType storageType;
    private String projectId;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
