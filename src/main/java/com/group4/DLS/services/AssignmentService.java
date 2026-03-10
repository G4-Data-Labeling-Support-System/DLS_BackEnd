package com.group4.DLS.services;

import com.group4.DLS.domain.dto.request.AssignmentCreateRequest;
import com.group4.DLS.domain.dto.request.AssignmentUpdateRequest;
import com.group4.DLS.domain.dto.response.AssignmentResponse;
import com.group4.DLS.domain.entity.Assignment;
import com.group4.DLS.domain.entity.Dataset;
import com.group4.DLS.domain.entity.Project;
import com.group4.DLS.domain.entity.User;
import com.group4.DLS.domain.entity.enums.AssignmentStatus;
import com.group4.DLS.exceptions.AppException;
import com.group4.DLS.exceptions.enums.ErrorCode;
import com.group4.DLS.mappers.AssignmentMapper;
import com.group4.DLS.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AssignmentService {
    AssignmentRepository assignmentRepository;
    AssignmentMapper assignmentMapper;
    ProjectRepository projectRepository;
    DatasetRepository datasetRepository;
    ActivityLogService logService;
    UserRepository userRepository;
    TaskRepository taskRepository;
    TaskService taskService;

    // ================= GET ALL ASSIGNMENTS =================
    public List<AssignmentResponse> getAllAssignments() {
        List<AssignmentResponse> assignments = assignmentRepository.findAll()
                .stream()
                .map(assignmentMapper::toResponse)
                .toList();
        if(assignments.isEmpty()){
            throw new AppException(ErrorCode.ASSIGNMENT_NOT_FOUND);
        }
        return assignments;
    }

    //get assignment by annotatorId
    public List<AssignmentResponse> getAssignmentForAnnotator(String annotatorId){
        //check user exist
        if(!userRepository.existsById(annotatorId)){
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        //get assignment by annotatorId
        List<AssignmentResponse> assignments = assignmentRepository.findByAssignedTo_UserId(annotatorId)
                .stream()
                .map(assignmentMapper::toResponse)
                .toList();
        //check if empty
        if(assignments.isEmpty()){
            throw new AppException(ErrorCode.ASSIGNMENT_NOT_FOUND);
        }
        return assignments;
    }

    //get assignment by id
    public AssignmentResponse getAssignmentById(String assignmentId){
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new AppException(ErrorCode.ASSIGNMENT_NOT_FOUND));
        return assignmentMapper.toResponse(assignment);
    }
    //get assignments for project
    public List<AssignmentResponse> getAssignmentForProject(String projectId){
        //check project exist
        if(!projectRepository.existsById(projectId)){
            throw new AppException(ErrorCode.PROJECT_NOT_FOUND);
        }
        List<AssignmentResponse> assignments = assignmentRepository.findByProject_ProjectId(projectId)
                .stream()
                .map(assignmentMapper::toResponse)
                .toList();
        if(assignments.isEmpty()){
            throw new AppException(ErrorCode.ASSIGNMENT_NOT_FOUND);
        }
        return assignments;
    }

//Create Assignment
    public AssignmentResponse createAssignment(String projectId,@RequestBody AssignmentCreateRequest request) {

    User manager = userRepository.findById(request.getAssignedBy())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    if(!"MANAGER".equalsIgnoreCase(manager.getRole().toString())) {
        throw new AppException(ErrorCode.USER_NOT_MANAGER);
    }


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

        Assignment assignment = new Assignment();
        assignment.setAssignedTo(assignedTo);
        assignment.setAssignedBy(manager);
        assignment.setReviewedBy(reviewedBy);
        assignment.setAssignmentName(request.getAssignmentName());
        assignment.setDescription(request.getDescription());
        assignment.setDataset(dataset);
        assignment.setProject(project);
        assignment.setDueDate(request.getDueDate());
        assignment.setAssignmentStatus(AssignmentStatus.ASSIGNED);
        assignment.setTotalItems(dataset.getTotalItems());
        dataset.setAssignment(assignment);

        assignmentRepository.save(assignment);
        datasetRepository.save(dataset);

//         // Log action
//        logService.log(
//                "CREATE_ASSIGNMENT",
//                "ASSIGNMENT",
//                assignment.getAssignmentId(),
//                "Created assignment: " + assignment.getAssignmentName());

        return assignmentMapper.toResponse(assignment);
    }

    //Update Assignment
    public AssignmentResponse updateAssignment(String assignmentId, AssignmentUpdateRequest request) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new AppException(ErrorCode.ASSIGNMENT_NOT_FOUND));

        User assignedTo = userRepository.findById(request.getAssignedTo())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        User reviewedBy = userRepository.findById(request.getReviewedBy())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        //check name is null or exists
        if (request.getAssignmentName() != null
                && !assignmentRepository.existsByAssignmentName(request.getAssignmentName())) {
            assignment = assignmentMapper.updateAssignmentFromRequest(request);
            assignment.setUpdateAt(LocalDateTime.now());
            assignment.setAssignedTo(assignedTo);
            assignment.setReviewedBy(reviewedBy);
        }

        assignmentRepository.save(assignment);

        // Log action
        logService.log(
                "UPDATE_ASSIGNMENT",
                "ASSIGNMENT",
                assignment.getAssignmentId(),
                "Assignment updated: " + assignment.getAssignmentName());

        return assignmentMapper.toResponse(assignment);
    }

    //Delete Assignment (soft delete by setting status to CANCELED)
    public void deleteAssignment(String assignmentId) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new AppException(ErrorCode.ASSIGNMENT_NOT_FOUND));

        assignment.setAssignmentStatus(AssignmentStatus.CANCLED);
        assignmentRepository.save(assignment);

        // Log action
        logService.log(
                "REMOVE_ASSIGNMENT",
                "ASSIGNMENT",
                assignment.getAssignmentId(),
                "Assignment removed: " + assignment.getAssignmentName());
    }
}
