package com.group4.DLS.mapper;

import com.group4.DLS.domain.dto.request.DatasetCreationRequest;
import com.group4.DLS.domain.dto.request.DatasetUpdateRequest;
import com.group4.DLS.domain.dto.response.DatasetResponse;
import com.group4.DLS.domain.entity.Dataset;
import com.group4.DLS.domain.entity.Project;
import org.springframework.stereotype.Component;

@Component
public class DatasetMapper {

    public Dataset toDataset(DatasetCreationRequest request, Project project) {
        Dataset dataset = new Dataset();
        dataset.setDatasetName(request.getDatasetName());
        dataset.setVersion(request.getVersion());
        dataset.setStorageType(request.getStorageType());
        dataset.setProject(project);
        return dataset;
    }

    public void updateDataset(DatasetUpdateRequest request, Dataset dataset) {
        dataset.setDatasetName(request.getDatasetName());
        dataset.setStorageType(request.getStorageType());
    }

    public DatasetResponse toResponse(Dataset dataset) {
        return DatasetResponse.builder()
                .datasetId(dataset.getDatasetId())
                .datasetName(dataset.getDatasetName())
                .version(dataset.getVersion())
                .storageType(dataset.getStorageType())
                .projectId(dataset.getProject().getProjectId())
                .createdAt(dataset.getCreatedAt())
                .updatedAt(dataset.getUpdatedAt())
                .build();
    }
}
