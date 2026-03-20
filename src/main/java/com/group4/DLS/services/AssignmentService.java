package com.group4.DLS.services;

import com.group4.DLS.domain.dto.request.AssignmentCreateRequest;
import com.group4.DLS.domain.dto.request.AssignmentDatasetChangeRequest;
import com.group4.DLS.domain.dto.request.AssignmentUpdateRequest;
import com.group4.DLS.domain.dto.response.AssignmentResponse;
import com.group4.DLS.domain.dto.response.DatasetResponse;
import com.group4.DLS.domain.dto.response.LabelResponse;
import com.group4.DLS.domain.dto.response.TaskResponse;
import com.group4.DLS.domain.entity.Assignment;
import com.group4.DLS.domain.entity.Dataset;
import com.group4.DLS.domain.entity.Project;
import com.group4.DLS.domain.entity.Task;
import com.group4.DLS.domain.entity.User;
import com.group4.DLS.domain.enums.AssignmentStatus;
import com.group4.DLS.domain.enums.TaskStatus;
import com.group4.DLS.exceptions.AppException;
import com.group4.DLS.exceptions.enums.ErrorCode;
import com.group4.DLS.mappers.AssignmentMapper;
import com.group4.DLS.mappers.DatasetMapper;
import com.group4.DLS.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.boot.actuate.autoconfigure.metrics.MetricsProperties.Data;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AssignmentService {
    AssignmentRepository assignmentRepository;
    ProjectRepository projectRepository;
    ProjectMemberRepository projectMemberRepository;
    DatasetRepository datasetRepository;
    UserRepository userRepository;
    TaskDataItemRepository taskDataItemRepository;
    TaskRepository taskRepository;

    TaskService taskService;
    LabelService labelService;
    ActivityLogService logService;
    AnnotationService annotationService;
    TaskDataItemService taskDataItemService;
    ProjectMemberService projectMemberService;
    ReviewService reviewService;

    AssignmentMapper assignmentMapper;
    DatasetMapper datasetMapper;


    // ================= GET ALL ASSIGNMENTS =================
    public List<AssignmentResponse> getAllAssignments() {
        List<AssignmentResponse> assignments = assignmentRepository.findAll()
                .stream()
                .map(assignmentMapper::toResponse)
                .toList();
        if (assignments.isEmpty()) {
            throw new AppException(ErrorCode.ASSIGNMENT_NOT_FOUND);
        }
        return assignments;
    }

    // ================= GET ASSIGNMENT BY ANNOTATOR_ID =================
    public List<AssignmentResponse> getAssignmentForAnnotator(String annotatorId) {
        // check user exist
        if (!userRepository.existsById(annotatorId)) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        // get assignment by annotatorId
        List<AssignmentResponse> assignments = assignmentRepository.findByAssignedTo_UserId(annotatorId)
                .stream()
                .map(assignmentMapper::toResponse)
                .toList();

        // check if empty
        if (assignments.isEmpty()) {
            throw new AppException(ErrorCode.ASSIGNMENT_NOT_FOUND);
        }

        return assignments;
    }

    // ================= GET ASSIGNMENT BY ASSIGNMENT_ID =================
    public AssignmentResponse getAssignmentById(String assignmentId) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new AppException(ErrorCode.ASSIGNMENT_NOT_FOUND));
        return assignmentMapper.toResponse(assignment);
    }

    // ================= GET LABELS BY ASSIGNMENT_ID =================
    public List<LabelResponse> getLabelsForAssignment(String assignmentId) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new AppException(ErrorCode.ASSIGNMENT_NOT_FOUND));

        // check dataset exist
        Dataset dataset = assignment.getDataset();
        if (dataset == null) {
            throw new AppException(ErrorCode.DATASET_NOT_FOUND);
        }

        return labelService.getAllByDataset(dataset.getDatasetId());// Get labels for the dataset associated with the
                                                                    // assignment

    }

    // ================= GET ASSIGNMENT FOR PRORJECT =================
    public List<AssignmentResponse> getAssignmentForProject(String projectId) {
        // check project exist
        if (!projectRepository.existsById(projectId)) {
            throw new AppException(ErrorCode.PROJECT_NOT_FOUND);
        }
        List<AssignmentResponse> assignments = assignmentRepository.findByProject_ProjectId(projectId)
                .stream()
                .map(assignmentMapper::toResponse)
                .toList();
        if (assignments.isEmpty()) {
            throw new AppException(ErrorCode.ASSIGNMENT_NOT_FOUND);
        }
        return assignments;
    }

    // ================= GET DATASET BY ASSIGNMENT_ID =================
    public DatasetResponse getDatasetByAssignmentId(String assignmentId) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new AppException(ErrorCode.ASSIGNMENT_NOT_FOUND));

        return datasetMapper.toDatasetResponse(assignment.getDataset());
    }

    // ================= GET ASSIGNMENT BY DATASET_ID =================
    public AssignmentResponse getAssignmentByDatasetId(String datasetId) {
        
        // Check dataset exist for current assignment
        Assignment assignment = assignmentRepository.findByDatasetDatasetId(datasetId);

        if (assignment == null) {
            throw new AppException(ErrorCode.DATASET_NOT_FOUND);
        } 

        return assignmentMapper.toResponse(assignment);
    }

    // ================= CREATE NEW ASSIGNMENT =================
    public AssignmentResponse createAssignment(String projectId, @RequestBody AssignmentCreateRequest request) {

        User manager = userRepository.findById(request.getAssignedBy())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUND));

        Dataset dataset = datasetRepository.findById(request.getDatasetId())
                .orElseThrow(() -> new AppException(ErrorCode.DATASET_NOT_FOUND));

        // Optional: kiểm tra dataset có thuộc project không
        if (dataset.getProject() == null ||
                !dataset.getProject().getProjectId().equals(projectId)) {
            throw new AppException(ErrorCode.DATASET_NOT_FOUND);
        }

        User assignedTo = userRepository.findById(request.getAssignedTo())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        User reviewedBy = userRepository.findById(request.getReviewedBy())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Assignment assignment = assignmentMapper.toAssignment(request);
        assignment.setAssignedTo(assignedTo);
        assignment.setAssignedBy(manager);
        assignment.setReviewedBy(reviewedBy);
        assignment.setDataset(dataset);
        assignment.setProject(project);
        assignment.setAssignmentStatus(AssignmentStatus.ASSIGNED);
        assignment.setTotalItems(dataset.getTotalItems());

        dataset.setAssignment(assignment);

        assignmentRepository.save(assignment);

        if (!projectMemberRepository.existsByProject_ProjectIdAndUser_UserId(projectId, assignedTo.getUserId())) {
            projectMemberService.assignMemberToProject(projectId, assignedTo.getUserId());
        }
        if (!projectMemberRepository.existsByProject_ProjectIdAndUser_UserId(projectId, reviewedBy.getUserId())) {
            projectMemberService.assignMemberToProject(projectId, reviewedBy.getUserId());
        }
        if (!projectMemberRepository.existsByProject_ProjectIdAndUser_UserId(projectId, manager.getUserId())) {
            projectMemberService.assignMemberToProject(projectId, manager.getUserId());
        }

        datasetRepository.save(dataset);

        taskService.createTasksForAssignment(assignment.getAssignmentId());
        
        assignmentRepository.save(assignment);

        // // Log action
        // logService.log(
        // "CREATE_ASSIGNMENT",
        // "ASSIGNMENT",
        // assignment.getAssignmentId(),
        // "Created assignment: " + assignment.getAssignmentName());

        return assignmentMapper.toResponse(assignment);
    }

    // ================= UPDATE CURRENT ASSIGNMENT =================
    public AssignmentResponse updateAssignment(String assignmentId, AssignmentUpdateRequest request) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new AppException(ErrorCode.ASSIGNMENT_NOT_FOUND));

        User assignedTo = userRepository.findById(request.getAssignedTo())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        User reviewedBy = userRepository.findById(request.getReviewedBy())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // check name is null or exists
        if (request.getAssignmentName() != null
                && !assignmentRepository.existsByAssignmentName(request.getAssignmentName())) {
            assignmentMapper.updateAssignmentFromRequest(request, assignment);
            assignment.setUpdateAt(LocalDateTime.now());
            assignment.setAssignedTo(assignedTo);
            assignment.setReviewedBy(reviewedBy);
        }

        assignmentRepository.save(assignment);

        // Log action
        // logService.log(
        //         "UPDATE_ASSIGNMENT",
        //         "ASSIGNMENT",
        //         assignment.getAssignmentId(),
        //         "Assignment updated: " + assignment.getAssignmentName());

        return assignmentMapper.toResponse(assignment);
    }

    // ================= CHANGE DATASET FOR CURRENT ASSIGNMENT =================
    public AssignmentResponse changeDatasetForCurrentAssignment(String assignmentId, AssignmentDatasetChangeRequest request) {

        // Get current assignment
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new AppException(ErrorCode.ASSIGNMENT_NOT_FOUND));

        // Check assignment status, if in-progress then not allow to change
        AssignmentStatus assignmentStatus = assignment.getAssignmentStatus();

        if (assignmentStatus.equals(AssignmentStatus.ASSIGNED)) {

            // Remove tasks that related to this assignment
            List<Task> tasks = taskRepository.findByAssignment_AssignmentId(assignmentId);
            for (Task task : tasks) {
                task.setTaskStatus(TaskStatus.INACTIVE);
            }
            taskRepository.saveAll(tasks);

            // Remove related TaskDataItem
            taskDataItemRepository.deleteByTask_Assignment_AssignmentId(assignmentId);

            // Get new dataset
            Dataset dataset = datasetRepository.findById(request.getDatasetId())
                    .orElseThrow(() ->  new AppException(ErrorCode.DATASET_NOT_FOUND));

            // Check if current dataset is being use by other assignment
            boolean isUsed = assignmentRepository.existsByDataset_DatasetIdAndAssignmentIdNot(dataset.getDatasetId(), assignmentId);

            if (isUsed) {
                throw new AppException(ErrorCode.DATASET_ALREADY_IN_USE);
            }
            
            // Clear old dataset
            Dataset oldDataset = assignment.getDataset();
            if (oldDataset != null) {
                oldDataset.setAssignment(null);
            } 

            // Set new dataset for assignment
            assignment.setDataset(dataset);

            // Set new assignment for dataset
            dataset.setAssignment(assignment);

            // Recreate task for current assignment
            taskService.createTasksForAssignment(assignment.getAssignmentId());

        } else {
            throw new AppException(ErrorCode.ASSIGNMENT_BUSY);
        }

        return assignmentMapper.toResponse(assignmentRepository.save(assignment));
    }

    // ================= REMOVE CURRENT ASSIGNMENT =================
    public void removeAssignment(String assignmentId) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new AppException(ErrorCode.ASSIGNMENT_NOT_FOUND));

        // Remove related Annotations
        annotationService.removeAnnotationByAssignmentId(assignmentId);

        // Unmap Task and Dataitem from TaskItem
        // taskDataItemService.deleteTaskDataItemsByAssignmentId(assignmentId);

        // Remove related Tasks
        taskService.removeTasksByAssignmentId(assignmentId);

        // Remove assignment reference from dataset
        if (!datasetRepository.findById(assignment.getDataset().getDatasetId()).isEmpty()) {
            Dataset dataset = assignment.getDataset();

            dataset.setAssignment(null);

            datasetRepository.save(dataset);
        }

        assignment.setAssignmentStatus(AssignmentStatus.INACTIVE);// Soft delete assignment
        
        assignmentRepository.save(assignment);

        // // Log action
        // logService.log(
        // "REMOVE_ASSIGNMENT",
        // "ASSIGNMENT",
        // assignment.getAssignmentId(),
        // "Assignment removed: " + assignment.getAssignmentName());
    }
}