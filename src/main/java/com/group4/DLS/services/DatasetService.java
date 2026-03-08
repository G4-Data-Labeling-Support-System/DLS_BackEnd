package com.group4.DLS.services;

import com.group4.DLS.domain.dto.request.DatasetCreationRequest;
import com.group4.DLS.domain.dto.request.DatasetUpdateRequest;
import com.group4.DLS.domain.dto.response.DatasetResponse;
import com.group4.DLS.domain.entity.Dataset;
import com.group4.DLS.domain.entity.Project;
import com.group4.DLS.exceptions.AppException;
import com.group4.DLS.exceptions.enums.ErrorCode;
import com.group4.DLS.mappers.DatasetMapper;
import com.group4.DLS.repositories.DatasetRepository;
import com.group4.DLS.repositories.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DatasetService {

    private final DatasetRepository datasetRepository;
    private final ProjectRepository projectRepository;
    private final DatasetMapper datasetMapper;
    private final DataitemService dataitemService;


    //List all dataset
    public List<DatasetResponse> getAllDatasets() {
        List<Dataset> datasets = datasetRepository.findAll();
        if (datasets.isEmpty()) {
            throw new AppException(ErrorCode.DATASET_NOT_FOUND);
        }
        return datasetMapper.toDatasetResponse(datasets);
    }

    // ===== LIST ALL DATASET FOR TARGET PROJECT =====
    public List<DatasetResponse> getAllDatasetForProject(String projectId) {
        try {
            if (projectId == null) {
                throw new AppException(ErrorCode.REQUIRE_PROJECT_ID);
            }

            Project project = projectRepository.findById(projectId)
                    .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUND));

            List<Dataset> datasets = datasetRepository.findByProject_ProjectId(project.getProjectId());

            return datasetMapper.toDatasetResponse(datasets);
        } catch (AppException ex) {
            throw new AppException(ErrorCode.DATASET_NOT_FOUND);
        }
    }

    // ===== CREATE DATASET =====
    public DatasetResponse createDataset(DatasetCreationRequest request) throws IOException {

        // Validate project exist
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUND));

        // Check if this dataset name exist inside this project
        if (datasetRepository.existsByProject_ProjectIdAndDatasetName(project.getProjectId(), request.getDatasetName())) {
            throw new AppException(ErrorCode.DATASET_ALREADY_EXISTS);
        }

        // Map request -> entity
        Dataset dataset = datasetMapper.createDatasetFromRequest(request);
        // Set project relationship
        dataset.setProject(project);
        datasetRepository.save(dataset);
        dataset.setTotalItems(dataitemService.createDataitem(dataset.getDatasetId(), request.getFiles()));
        datasetRepository.save(dataset);


        // Save and return response
        return datasetMapper.toDatasetResponse(dataset);
    }

    // ===== UPDATE DATASET =====
    public DatasetResponse updateDatasetResponse(String datasetId, DatasetUpdateRequest request) {
        Dataset dataset = datasetRepository.findById(datasetId)
                .orElseThrow(() -> new AppException(ErrorCode.DATASET_NOT_FOUND));

        // Check duplicate name (If name change)
        if (request.getDatasetName() != null && !request.getDatasetName().equals(dataset.getDatasetName())) {
            throw new AppException(ErrorCode.DATASETNAME_ALREADY_EXSITS);
        }

        // Capture old values for logging
        String oldName = dataset.getDatasetName();
        String oldDescription = dataset.getDescription();

        // Update entity
        datasetMapper.updateDatasetFromRequest(request, dataset);

        // Save and return response
        return datasetMapper.toDatasetResponse(datasetRepository.save(dataset));
    }

    // ===== DELETE DATASET =====
    // public void deleteDataset(String datasetId) {

    //     User currentUser = currentUserProvider.getCurrentUser();

    //     if (currentUser.getUserRole() != UserRole.MANAGER) {
    //         throw new AppException(ErrorCode.FORBIDDEN);
    //     }

    //     Dataset dataset = datasetRepository.findById(datasetId)
    //             .orElseThrow(() -> new AppException(ErrorCode.DATASET_NOT_FOUND));

    //     datasetRepository.delete(dataset);
    // }
}