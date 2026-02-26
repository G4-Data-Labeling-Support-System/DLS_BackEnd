package com.group4.DLS.domain.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

import com.group4.DLS.domain.entity.enums.DatasetStorageType;

@Getter
@Builder
public class DatasetResponse {
    private String datasetId;
    private String datasetName;
    private int version;
    private DatasetStorageType storageType;
    private LocalDate createdAt;
    private LocalDate updatedAt;

}
