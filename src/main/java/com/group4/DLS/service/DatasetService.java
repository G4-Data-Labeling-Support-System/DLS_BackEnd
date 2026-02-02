package com.group4.DLS.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.group4.DLS.domain.dto.request.DatasetCreationRequest;
import com.group4.DLS.domain.dto.request.DatasetUpdateRequest;
import com.group4.DLS.domain.dto.response.DatasetResponse;
import com.group4.DLS.domain.entity.Dataset;
import com.group4.DLS.domain.entity.Dataitem;
import com.group4.DLS.domain.entity.Project;
import com.group4.DLS.domain.entity.User;
import com.group4.DLS.domain.entity.enums.DatasetStatus;
import com.group4.DLS.domain.entity.enums.UserRole;
import com.group4.DLS.domain.entity.enums.UserStatus;
import com.group4.DLS.exceptions.AppException;
import com.group4.DLS.exceptions.enums.ErrorCode;
import com.group4.DLS.mapper.DatasetMapper;
import com.group4.DLS.repository.DatasetRepository;
import com.group4.DLS.repository.DataitemRepository;
import com.group4.DLS.repository.ProjectRepository;
import com.group4.DLS.security.CurrentUserProvider;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class DatasetService {

    DatasetRepository datasetRepository;
    ProjectRepository projectRepository;
    DataitemRepository dataitemRepository;
    DatasetMapper datasetMapper;
    CurrentUserProvider currentUserProvider;

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

        //  Set status = EMPTY khi tạo mới
        dataset.setStatus(DatasetStatus.EMPTY);

        return datasetMapper.toResponse(datasetRepository.save(dataset));
    }

    // ===== GET ALL DATASETS BY PROJECT =====
    public List<DatasetResponse> getAllDatasetByProject(String projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUND));

        return datasetRepository.findByProject(project)
                .stream()
                .map(datasetMapper::toResponse)
                .toList();
    }

    // ===== UPDATE DATASET =====
    public DatasetResponse updateDataset(String id, DatasetUpdateRequest request) {
        User currentUser = currentUserProvider.getCurrentUser();

        if (currentUser.getUserRole() != UserRole.MANAGER) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }

        Dataset dataset = datasetRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DATASET_NOT_FOUND));

        //  Kiểm tra LOCKED
        if (dataset.getStatus() == DatasetStatus.LOCKED) {
            throw new AppException(ErrorCode.DATASET_LOCKED);
        }

        datasetMapper.updateDataset(request, dataset);

        return datasetMapper.toResponse(datasetRepository.save(dataset));
    }

    // ===== DELETE DATASET =====
    public void deleteDataset(String id) {
        User currentUser = currentUserProvider.getCurrentUser();

        if (currentUser.getUserRole() != UserRole.MANAGER) {
            throw new AppException(ErrorCode.FORBIDDEN);
        }

        Dataset dataset = datasetRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DATASET_NOT_FOUND));

        //  Kiểm tra IN_USE
        if (dataset.getStatus() == DatasetStatus.IN_USE) {
            throw new AppException(ErrorCode.DATASET_IN_USE);
        }

        datasetRepository.delete(dataset);
    }

    // ===== UPLOAD DATA ITEM (Helper method) =====
    public void uploadDataItem(String datasetId, Dataitem dataItem) {
        Dataset dataset = datasetRepository.findById(datasetId)
                .orElseThrow(() -> new AppException(ErrorCode.DATASET_NOT_FOUND));

        //  Kiểm tra LOCKED
        if (dataset.getStatus() == DatasetStatus.LOCKED) {
            throw new AppException(ErrorCode.DATASET_LOCKED);
        }

        // Set relationship
        dataItem.setDataset(dataset);
        dataitemRepository.save(dataItem);

        // ✅ Tự động chuyển EMPTY → READY khi có DataItem đầu tiên
        if (dataset.getStatus() == DatasetStatus.EMPTY) {
            dataset.setStatus(DatasetStatus.READY);
            datasetRepository.save(dataset);
        }
    }

    // ===== CHANGE STATUS TO IN_USE =====
    public void markDatasetAsInUse(String datasetId) {
        Dataset dataset = datasetRepository.findById(datasetId)
                .orElseThrow(() -> new AppException(ErrorCode.DATASET_NOT_FOUND));

        // Chỉ Dataset READY mới có thể chuyển sang IN_USE
        if (dataset.getStatus() == DatasetStatus.EMPTY) {
            throw new AppException(ErrorCode.DATASET_EMPTY);
        }

        // Tự động chuyển sang IN_USE
        dataset.setStatus(DatasetStatus.IN_USE);
        datasetRepository.save(dataset);
    }
}