package com.group4.DLS.services;

import com.group4.DLS.domain.dto.request.DatasetCreationRequest;
import com.group4.DLS.domain.dto.request.DatasetUpdateRequest;
import com.group4.DLS.domain.dto.response.DatasetResponse;
import com.group4.DLS.domain.entity.Assignment;
import com.group4.DLS.domain.entity.Dataset;
import com.group4.DLS.domain.entity.Project;
import com.group4.DLS.domain.enums.DatasetStatus;
import com.group4.DLS.exceptions.AppException;
import com.group4.DLS.exceptions.enums.ErrorCode;
import com.group4.DLS.mappers.DatasetMapper;
import com.group4.DLS.repositories.DataItemRepository;
import com.group4.DLS.repositories.DatasetRepository;
import com.group4.DLS.repositories.ProjectRepository;
import com.group4.DLS.repositories.TaskDataItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DatasetService {

    private final DatasetRepository datasetRepository;
    private final ProjectRepository projectRepository;
    private final DatasetMapper datasetMapper;
    private final DataitemService dataitemService;
    private final TaskDataItemRepository taskDataItemRepository;
    private final AssignmentService assignmentService;

    // ===== LIST ALL DATASET =====
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

    // ===== GET DATASET BY ID =====
    public DatasetResponse getDatasetById(String datasetId) {
        Dataset dataset = datasetRepository.findById(datasetId)
                .orElseThrow(() -> new AppException(ErrorCode.DATASET_NOT_FOUND));
        return datasetMapper.toDatasetResponse(dataset);
    }

    // ===== CREATE DATASET =====
    public DatasetResponse createDataset(DatasetCreationRequest request) throws IOException {

        // Validate project exist
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUND));

        // Check if this dataset name exist inside this project
        if (datasetRepository.existsByProjectProjectIdAndDatasetNameAndDatasetStatusNot(project.getProjectId(),
                request.getDatasetName(), DatasetStatus.INACTIVE)) {
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
    public DatasetResponse updateDataset(String datasetId, DatasetUpdateRequest request) throws IOException{

        Dataset dataset = datasetRepository.findById(datasetId)
                .orElseThrow(() -> new AppException(ErrorCode.DATASET_NOT_FOUND));
        // Validate project exist
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUND));
        // Check if this dataset name exist inside this project
        if (!request.getDatasetName().equals(dataset.getDatasetName()) && datasetRepository.existsByProjectProjectIdAndDatasetNameAndDatasetStatusNot(project.getProjectId(), request.getDatasetName(), DatasetStatus.INACTIVE)) {
            throw new AppException(ErrorCode.DATASET_ALREADY_EXISTS);
        }

        // update basic info
        datasetMapper.updateDatasetFromRequest(request, dataset);
        dataset.setProject(project);

        //check do dataset have assignment
        Assignment hasAssignment = dataset.getAssignment();

        // delete dataitems
        if (request.getDeleteDataItemId() != null && !request.getDeleteDataItemId().isEmpty()) {
            if (hasAssignment != null) {
                throw new AppException(ErrorCode.CANNOT_DELETE_DATAIEM_AFTER_ASSIGN_ASSIGNMENT);
            }

            for(String dataItemId: request.getDeleteDataItemId()) {
                dataitemService.deleteDataitem(dataItemId);
            }
        }


        List<MultipartFile> files = request.getFiles();

        if(files != null && files.stream().anyMatch(file -> !file.isEmpty())){
            dataitemService.createDataitem(dataset.getDatasetId(), request.getFiles());//insert and return new item
            if (!(hasAssignment == null)) {
                if (files.size() < 20) {
                    // rule: chỉ được thêm và phải >=20
                    throw new AppException(ErrorCode.DATAITEM_MINIMUM_REQUIRED);
                }
                dataitemService.assignNewDataItems(dataset.getDatasetId());//insert and return new item
            }
        }
        datasetRepository.save(dataset);
        dataset.setTotalItems(dataset.getDataitems().size());
        datasetRepository.save(dataset);

        return datasetMapper.toDatasetResponse(dataset) ;
    }

    // ===== DELETE DATASET =====
    @Transactional
    public void deleteDataset(String datasetId) {

        // Check dataset exist
        Dataset dataset = datasetRepository.findById(datasetId)
                .orElseThrow(() -> new AppException(ErrorCode.DATASET_NOT_FOUND));

        // Delete all dataitems, taskdataitems, and assignment related to this dataset
        taskDataItemRepository.deleteByDataitem_Dataset_DatasetId(dataset.getDatasetId());// delete taskdataitem

        if (dataset.getAssignment() != null) {
            assignmentService.removeAssignment(dataset.getAssignment().getAssignmentId());// Delete assignment
        }

        dataitemService.deleteDataitemsByDatasetId(dataset.getDatasetId());// Delete dataitems
        dataset.setDatasetStatus(DatasetStatus.INACTIVE);// Soft delete dataset
        
        datasetRepository.save(dataset);
    }
}