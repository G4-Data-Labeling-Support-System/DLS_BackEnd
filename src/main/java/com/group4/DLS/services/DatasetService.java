package com.group4.DLS.services;

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

import java.util.List;

@Service
@RequiredArgsConstructor
public class DatasetService {

    private final DatasetRepository datasetRepository;
    private final ProjectRepository projectRepository;
    private final DatasetMapper datasetMapper;

    // ===== LIST ALL DATASET FOR TARGET PROJECT =====
    public List<DatasetResponse> getAllDatasetForProject(String projectId) {
        try {
            Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUND));

            List<Dataset> datasets = datasetRepository.findByProject_ProjectId(project.getProjectId());

            return datasetMapper.toDatasetResponse(datasets);
        } catch (AppException ex) {
            throw new AppException(ErrorCode.DATASET_NOT_FOUND);
        }
    }

    // ===== CREATE DATASET =====
    // public DatasetResponse createDataset(DatasetCreationRequest request) {
    //     Project project = projectRepository.findById(request.getProjectId())
    //             .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUND));

    //     Dataset dataset = datasetMapper.toDataset(request, project);
    //     return datasetMapper.toResponse(datasetRepository.save(dataset));
    // }

    // ===== UPDATE DATASET =====
    // public DatasetResponse updateDataset(String datasetId, DatasetUpdateRequest request) {

    //     User currentUser = currentUserProvider.getCurrentUser();

    //     if (currentUser.getUserRole() != UserRole.MANAGER) {
    //         throw new AppException(ErrorCode.FORBIDDEN);
    //     }

    //     Dataset dataset = datasetRepository.findById(datasetId)
    //             .orElseThrow(() -> new AppException(ErrorCode.DATASET_NOT_FOUND));

    //     datasetMapper.updateDataset(request, dataset);
    //     return datasetMapper.toResponse(datasetRepository.save(dataset));
    // }

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
