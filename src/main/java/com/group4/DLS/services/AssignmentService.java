package com.group4.DLS.services;

import com.group4.DLS.aop.LogActivity;
import com.group4.DLS.domain.dto.request.AssignmentCreateRequest;
import com.group4.DLS.domain.dto.request.AssignmentDatasetChangeRequest;
import com.group4.DLS.domain.dto.request.AssignmentUpdateRequest;
import com.group4.DLS.domain.dto.response.AssignmentResponse;
import com.group4.DLS.domain.dto.response.DatasetResponse;
import com.group4.DLS.domain.dto.response.LabelResponse;
import com.group4.DLS.domain.entity.Assignment;
import com.group4.DLS.domain.entity.Dataset;
import com.group4.DLS.domain.entity.Project;
import com.group4.DLS.domain.entity.Task;
import com.group4.DLS.domain.entity.User;
import com.group4.DLS.domain.enums.AssignmentStatus;
import com.group4.DLS.domain.enums.ProjectStatus;
import com.group4.DLS.domain.enums.TaskStatus;
import com.group4.DLS.exceptions.AppException;
import com.group4.DLS.exceptions.enums.ErrorCode;
import com.group4.DLS.mappers.AssignmentMapper;
import com.group4.DLS.mappers.DatasetMapper;
import com.group4.DLS.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    AnnotationRepository annotationRepository;

    TaskService taskService;
    TaskDataItemService taskDataItemService;
    LabelService labelService;
    AnnotationService annotationService;
    ProjectMemberService projectMemberService;

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

        for (AssignmentResponse assignment: assignments){
            updateAssignmentStatus(assignment.getAssignmentId());
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
                .filter(assignment -> assignment.getAssignmentStatus() != AssignmentStatus.INACTIVE)
                .map(assignmentMapper::toResponse)
                .toList();

        // check if empty
        if (assignments.isEmpty()) {
            throw new AppException(ErrorCode.ASSIGNMENT_NOT_FOUND);
        }

        for (AssignmentResponse assignment: assignments){
            updateAssignmentStatus(assignment.getAssignmentId());
        }

        return assignments;
    }

    // ================= GET ASSIGNMENT BY review_ID =================
    public List<AssignmentResponse> getAssignmentForReviewer(String reviewerId) {
        // check user exist
        if (!userRepository.existsById(reviewerId)) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        List<AssignmentResponse> assignments = assignmentRepository
                .findByReviewedBy_UserId(reviewerId)
                .stream()
                .filter(assignment -> assignment.getAssignmentStatus() != AssignmentStatus.INACTIVE)
                .map(assignmentMapper::toResponse)
                .toList();

        // check if empty
        if (assignments.isEmpty()) {
            throw new AppException(ErrorCode.ASSIGNMENT_NOT_FOUND);
        }

        for (AssignmentResponse assignment: assignments){
            updateAssignmentStatus(assignment.getAssignmentId());
        }

        return assignments;
    }

    // ================= GET ASSIGNMENT BY ASSIGNMENT_ID =================
    public AssignmentResponse getAssignmentById(String assignmentId) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new AppException(ErrorCode.ASSIGNMENT_NOT_FOUND));
        updateAssignmentStatus(assignment.getAssignmentId());
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

        updateAssignmentStatus(assignment.getAssignmentId());

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
        for(AssignmentResponse assignment: assignments){
            updateAssignmentStatus(assignment.getAssignmentId());
        }
        return assignments;
    }

    // ================= GET DATASET BY ASSIGNMENT_ID =================
    public DatasetResponse getDatasetByAssignmentId(String assignmentId) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new AppException(ErrorCode.ASSIGNMENT_NOT_FOUND));

        updateAssignmentStatus(assignment.getAssignmentId());
        return datasetMapper.toDatasetResponse(assignment.getDataset());
    }

    // ================= GET ASSIGNMENT BY DATASET_ID =================
    public AssignmentResponse getAssignmentByDatasetId(String datasetId) {
        
        // Check dataset exist for current assignment
        Assignment assignment = assignmentRepository.findByDatasetDatasetId(datasetId);

        if (assignment == null) {
            throw new AppException(ErrorCode.DATASET_NOT_FOUND);
        }

        updateAssignmentStatus(assignment.getAssignmentId());

        return assignmentMapper.toResponse(assignment);
    }

    // ================= CREATE NEW ASSIGNMENT =================
    @LogActivity(
        action = "CREATE",
        entity = "Assignment",
        description = "Create assignment",
        entityIdField = "assignmentId"
    )
    public AssignmentResponse createAssignment(String projectId, @RequestBody AssignmentCreateRequest request) {

        // Get current user info
        User manager = userRepository.findById(request.getAssignedBy())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Get current project
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUND));

        // Get current dataset
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

        // Create new assignment
        Assignment assignment = assignmentMapper.toAssignment(request);
        assignment.setAssignedTo(assignedTo);
        assignment.setAssignedBy(manager);
        assignment.setReviewedBy(reviewedBy);
        assignment.setDataset(dataset);
        assignment.setProject(project);
        assignment.setAssignmentStatus(AssignmentStatus.ASSIGNED);


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

        // Update project status after create new assignment
        project.setProjectStatus(ProjectStatus.IN_PROGRESS);

        return assignmentMapper.toResponse(assignment);
    }

    // ================= UPDATE CURRENT ASSIGNMENT =================
    @LogActivity(
        action = "UPDATE",
        entity = "Assignment",
        description = "Update assignment",
        entityIdParam = "assignmentId"
    )
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

        return assignmentMapper.toResponse(assignment);
    }

    // ================= CHANGE DATASET FOR CURRENT ASSIGNMENT =================
    @LogActivity(
        action = "UPDATE",
        entity = "Assignment",
        description = "Change assignment dataset",
        entityIdParam = "assignmentId"
    )
    public AssignmentResponse changeDatasetForCurrentAssignment(String assignmentId, AssignmentDatasetChangeRequest request) {

        // Get current assignment
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new AppException(ErrorCode.ASSIGNMENT_NOT_FOUND));

        // Check assignment status, if in-progress then not allow to change
        AssignmentStatus assignmentStatus = assignment.getAssignmentStatus();

        if (assignmentStatus.equals(AssignmentStatus.ASSIGNED)) {

            //remove annotation
            annotationRepository.deleteAllByTask_Assignment_AssignmentId(assignmentId);
            // Remove related TaskDataItem
            taskDataItemRepository.deleteByTask_Assignment_AssignmentId(assignmentId);
            // Remove tasks that related to this assignment
            taskRepository.deleteByAssignment_AssignmentId(assignmentId);

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
    @LogActivity(
        action = "DELETE",
        entity = "Assignment",
        description = "Delete assignment",
        entityIdParam = "assignmentId"
    )
    @Transactional(rollbackFor = Exception.class)
    public void removeAssignment(String assignmentId) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new AppException(ErrorCode.ASSIGNMENT_NOT_FOUND));

        // Remove assignment reference from dataset
        if (!datasetRepository.findById(assignment.getDataset().getDatasetId()).isEmpty()) {
            Dataset dataset = assignment.getDataset();

            dataset.setAssignment(null);

            datasetRepository.save(dataset);
        }
        // Remove related Annotations
        annotationService.removeAnnotationByAssignmentId(assignmentId);
        // Unmap Task and Dataitem from TaskItem
        taskDataItemService.deleteTaskDataItemsByAssignmentId(assignmentId);
        // Remove related Tasks
        taskService.removeTasksByAssignmentId(assignmentId);
        assignment.setDataset(null);
        assignment.setAssignmentStatus(AssignmentStatus.INACTIVE);// Soft delete assignment
        assignmentRepository.save(assignment);
    }


    public void updateAssignmentStatus(String assignmentId) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new AppException(ErrorCode.ASSIGNMENT_NOT_FOUND));

        List<Task> tasks = taskRepository.findByAssignment_AssignmentId(assignmentId);

        int countComplete = 0;
        for(Task task : tasks){
            countComplete += task.getCompletedCount();
        }

        boolean allNotStarted = tasks.stream()
                .allMatch(t -> t.getTaskStatus() == TaskStatus.NOT_STARTED);

        boolean anyInProgress = tasks.stream()
                .anyMatch(t -> t.getTaskStatus() == TaskStatus.IN_PROGRESS);

        boolean allReadyReview = tasks.stream()
                .anyMatch(t -> t.getTaskStatus() == TaskStatus.IN_REVIEW);

        boolean allDone = tasks.stream()
                .allMatch(t -> t.getTaskStatus() == TaskStatus.COMPLETED);

        if (LocalDateTime.now().isAfter(assignment.getDueDate())) {
            assignment.setAssignmentStatus(AssignmentStatus.OVER_DUE);
        }


        if(assignment.getAssignmentStatus() == AssignmentStatus.INACTIVE){
            assignment.setAssignmentStatus(AssignmentStatus.INACTIVE);
        }else if (allDone) {
            assignment.setAssignmentStatus(AssignmentStatus.COMPLETED);
        } else if (allReadyReview) {
            assignment.setAssignmentStatus(AssignmentStatus.REVIEWING);
        } else if (anyInProgress) {
            assignment.setAssignmentStatus(AssignmentStatus.IN_PROGRESS);
        } else if (allNotStarted) {
            assignment.setAssignmentStatus(AssignmentStatus.ASSIGNED);
        } 
        assignment.setCompletedItems(countComplete);
        assignmentRepository.save(assignment);
    }
}