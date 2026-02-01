package com.group4.DLS.services;

import com.group4.DLS.domain.dto.request.DatasetCreationRequest;
import com.group4.DLS.domain.dto.request.DatasetUpdateRequest;
import com.group4.DLS.domain.dto.response.DatasetResponse;
import com.group4.DLS.domain.entity.Dataset;
import com.group4.DLS.domain.entity.Project;
import com.group4.DLS.domain.entity.User;
import com.group4.DLS.domain.entity.enums.UserRole;
import com.group4.DLS.domain.entity.enums.UserStatus;
import com.group4.DLS.exceptions.AppException;
import com.group4.DLS.exceptions.enums.ErrorCode;
import com.group4.DLS.mappers.DatasetMapper;
import com.group4.DLS.repositories.DatasetRepository;
import com.group4.DLS.repositories.ProjectRepository;
import com.group4.DLS.security.CurrentUserProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DatasetService {

    private final DatasetRepository datasetRepository;
    private final ProjectRepository projectRepository;
    private final DatasetMapper datasetMapper;
    private final CurrentUserProvider currentUserProvider;

    // ===== CREATE DATASET =====
    public DatasetResponse createDataset(DatasetCreationRequest request) {

        User currentUser = currentUserProvider.getCurrentUser();

        if (currentUser.getStatus() != UserStatus.ACTIVE) {
            throw new AppException(ErrorCode.USER_NOT_ACTIVE);
        }

        if (currentUser.getUserRole() != UserRole.MANAGER) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }

        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUND));

        Dataset dataset = datasetMapper.toDataset(request, project);
        return datasetMapper.toResponse(datasetRepository.save(dataset));
    }

    // ===== LIST ALL DATASET OF PROJECT =====
    public List<DatasetResponse> getAllDatasetByProject(String projectId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUND));

        return datasetRepository.findByProject(project)
                .stream()
                .map(datasetMapper::toResponse)
                .toList();
    }

    // ===== UPDATE DATASET =====
    public DatasetResponse updateDataset(String datasetId, DatasetUpdateRequest request) {

        User currentUser = currentUserProvider.getCurrentUser();

        if (currentUser.getUserRole() != UserRole.MANAGER) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }

        Dataset dataset = datasetRepository.findById(datasetId)
                .orElseThrow(() -> new AppException(ErrorCode.DATASET_NOT_FOUND));

        datasetMapper.updateDataset(request, dataset);
        return datasetMapper.toResponse(datasetRepository.save(dataset));
    }

    // ===== DELETE DATASET =====
    public void deleteDataset(String datasetId) {

        User currentUser = currentUserProvider.getCurrentUser();

        if (currentUser.getUserRole() != UserRole.MANAGER) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }

        Dataset dataset = datasetRepository.findById(datasetId)
                .orElseThrow(() -> new AppException(ErrorCode.DATASET_NOT_FOUND));

        datasetRepository.delete(dataset);
    }
}
