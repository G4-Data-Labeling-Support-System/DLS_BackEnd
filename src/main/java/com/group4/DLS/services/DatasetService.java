package com.group4.DLS.services;

import com.group4.DLS.aop.LogActivity;
import com.group4.DLS.domain.dto.request.DatasetCreationRequest;
import com.group4.DLS.domain.dto.request.DatasetUpdateRequest;
import com.group4.DLS.domain.dto.response.DatasetResponse;
import com.group4.DLS.domain.entity.Assignment;
import com.group4.DLS.domain.entity.Dataitem;
import com.group4.DLS.domain.entity.Dataset;
import com.group4.DLS.domain.entity.Project;
import com.group4.DLS.domain.entity.Task;
import com.group4.DLS.domain.enums.AssignmentStatus;
import com.group4.DLS.domain.enums.DataItemStatus;
import com.group4.DLS.domain.enums.DatasetStatus;
import com.group4.DLS.domain.enums.TaskStatus;
import com.group4.DLS.exceptions.AppException;
import com.group4.DLS.exceptions.enums.ErrorCode;
import com.group4.DLS.mappers.DatasetMapper;
import com.group4.DLS.repositories.AssignmentRepository;
import com.group4.DLS.repositories.DataItemRepository;
import com.group4.DLS.repositories.DatasetRepository;
import com.group4.DLS.repositories.ProjectRepository;
import com.group4.DLS.repositories.TaskDataItemRepository;
import com.group4.DLS.repositories.TaskRepository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class DatasetService {

    DatasetRepository datasetRepository;
    ProjectRepository projectRepository;
    TaskDataItemRepository taskDataItemRepository;
    AssignmentRepository assignmentRepository;
    DataItemRepository dataItemRepository;
    TaskRepository taskRepository;

    DatasetMapper datasetMapper;

    DataitemService dataitemService;
    AnnotationService annotationService;
    LabelService labelService;

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
    @LogActivity(
        action = "CREATE",
        entity = "Dataset",
        description = "Create dataset",
        entityIdField = "datasetId"
    )
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
    @LogActivity(
        action = "UPDATE",
        entity = "Dataset",
        description = "Update dataset",
        entityIdParam = "datasetId"
    )
    public DatasetResponse updateDataset(String datasetId, DatasetUpdateRequest request,List<MultipartFile> files) throws IOException {

        Dataset dataset = datasetRepository.findById(datasetId)
                .orElseThrow(() -> new AppException(ErrorCode.DATASET_NOT_FOUND));
        // Validate project exist
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUND));
        // Check if this dataset name exist inside this project
        if (!request.getDatasetName().equals(dataset.getDatasetName())
                && datasetRepository.existsByProjectProjectIdAndDatasetNameAndDatasetStatusNot(project.getProjectId(),
                        request.getDatasetName(), DatasetStatus.INACTIVE)) {
            throw new AppException(ErrorCode.DATASET_ALREADY_EXISTS);
        }

        // update basic info
        datasetMapper.updateDatasetFromRequest(request, dataset);
        dataset.setProject(project);
        datasetRepository.save(dataset);

        // check do dataset have assignment
        Assignment hasAssignment = dataset.getAssignment();

        // delete dataitems
        if (request.getDeleteDataItemId() != null && !request.getDeleteDataItemId().isEmpty()) {
            if (hasAssignment != null) {
                throw new AppException(ErrorCode.CANNOT_DELETE_DATAIEM_AFTER_ASSIGN_ASSIGNMENT);
            }
            dataitemService.deleteDataitems(request.getDeleteDataItemId());
            int countDelete = request.getDeleteDataItemId().size();
            dataset.setTotalItems(dataset.getTotalItems()-countDelete);
        }


        if (files != null && files.stream().anyMatch(file -> !file.isEmpty())) {
            dataitemService.createDataitem(dataset.getDatasetId(), files);// insert and return new item
            if (!(hasAssignment == null)) {
                if (files.size() < 20) {
                    // rule: chỉ được thêm và phải >=20
                    throw new AppException(ErrorCode.DATAITEM_MINIMUM_REQUIRED);
                }
                dataitemService.assignNewDataItems(dataset.getDatasetId());// insert and return new item
            }
            dataset.setTotalItems(dataset.getDataitems().size());
        }
        datasetRepository.save(dataset);
        return datasetMapper.toDatasetResponse(dataset);
    }

    //get dataset have not assignment
    public List<DatasetResponse> getDatasetsNotHaveAssignmentInProject(String projectId){
        try {
            if (projectId == null) {
                throw new AppException(ErrorCode.REQUIRE_PROJECT_ID);
            }

            Project project = projectRepository.findById(projectId)
                    .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUND));

            List<Dataset> datasets = datasetRepository.findByAssignmentIsNullAndProject_ProjectIdAndDatasetStatus(project.getProjectId(), DatasetStatus.ACTIVE);

            return datasetMapper.toDatasetResponse(datasets);
        } catch (AppException ex) {
            throw new AppException(ErrorCode.DATASET_NOT_FOUND);
        }
    }

    // ===== DELETE DATASET =====
    @LogActivity(
        action = "REMOVE",
        entity = "Dataset",
        description = "Remove dataset",
        entityIdParam = "datasetId"
    )
    @Transactional
    public DatasetResponse deleteDataset(String datasetId) {
        
        // Check dataset exists (current dataset)
        Dataset dataset = datasetRepository.findById(datasetId)
                .orElseThrow(() -> new AppException(ErrorCode.DATASET_NOT_FOUND));

        // Assignment that have this dataset
        Assignment assignment = assignmentRepository.findByDatasetDatasetId(datasetId);

        // Case 1: No assignment -> allow to delete
        if (assignment == null) {
            performDelete(datasetId, dataset, assignment);
        } 
        // Case 2: Assignment exists but not BUSY -> allow to delete
        else if (assignment.getAssignmentStatus().equals(AssignmentStatus.ASSIGNED)) {
            performDelete(datasetId, dataset, assignment);
        } 
        // Case 3: Assignment BUSY -> not allow to delete
        else {
            throw new AppException(ErrorCode.ASSIGNMENT_BUSY);
        }

        return datasetMapper.toDatasetResponse(datasetRepository.save(dataset));
    }

    public void performDelete(String datasetId, Dataset dataset, Assignment assignment) {

        // Remove Dataitem
        List<Dataitem> dataitems = dataItemRepository.findByDataset_DatasetId(datasetId);
        for (Dataitem dataitem : dataitems) {
            dataitem.setDataItemStatus(DataItemStatus.INACTIVE);
        }

        if (assignment != null) {
            // Soft remove related annotation
            annotationService.removeAnnotationByAssignmentId(assignment.getAssignmentId());

            // Remove related TaskDataItem
            taskDataItemRepository.deleteByTask_Assignment_AssignmentId(assignment.getAssignmentId());

            // Remove tasks that related to this assignment
            List<Task> tasks = taskRepository.findByAssignment_AssignmentId(assignment.getAssignmentId());
            for (Task task : tasks) {
                task.setTaskStatus(TaskStatus.INACTIVE);
            }
            taskRepository.saveAll(tasks);
        }

        // Soft remove related labels
        labelService.deleteLabelsByDatasetId(datasetId);

        // Set dataset status to INACTIVE
        dataset.setDatasetStatus(DatasetStatus.INACTIVE);
    }
}