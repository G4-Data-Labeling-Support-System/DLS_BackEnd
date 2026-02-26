package com.group4.DLS.services;

import com.group4.DLS.domain.dto.request.AssignmentCreateRequest;
import com.group4.DLS.domain.dto.request.AssignmentUpdateRequest;
import com.group4.DLS.domain.dto.response.AssignmentResponse;
import com.group4.DLS.domain.entity.Assignment;
import com.group4.DLS.domain.entity.Dataset;
import com.group4.DLS.domain.entity.Project;
import com.group4.DLS.domain.entity.enums.AssignmentStatus;
import com.group4.DLS.domain.entity.enums.Status;
import com.group4.DLS.exceptions.AppException;
import com.group4.DLS.exceptions.enums.ErrorCode;
import com.group4.DLS.mappers.AssignmentMapper;
import com.group4.DLS.repositories.AssignmentRepository;
import com.group4.DLS.repositories.DatasetRepository;
import com.group4.DLS.repositories.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

//Create Assignment
    public AssignmentResponse createAssignment(String projectId, String datasetId, AssignmentCreateRequest request) {
        if (assignmentRepository.existsByAssignmentName(request.getAssignmentName())) {
            throw new AppException(ErrorCode.ASSIGNMENT_EXISTS);
        }
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUND));

        Dataset dataset = datasetRepository.findById(datasetId)
                .orElseThrow(() -> new AppException(ErrorCode.DATASET_NOT_FOUND));
        Assignment assignment = assignmentMapper.toAssignment(request);
        assignment.setAssignmentStatus(AssignmentStatus.CREATED);
        assignment.setStatus(Status.ACTIVE);
        assignment.setProject(project);
        assignment.setDataset(dataset);

        assignmentRepository.save(assignment);

         // Log action
        logService.log(
                "CREATE_ASSIGNMENT",
                "ASSIGNMENT",
                assignment.getAssignmentId(),
                "Created assignment: " + assignment.getAssignmentName());

        return assignmentMapper.toResponse(assignment);
    }

    //Update Assignment
    public AssignmentResponse updateAssignment(String assignmentId, AssignmentUpdateRequest request) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new AppException(ErrorCode.ASSIGNMENT_NOT_FOUND));

        if (request.getAssignmentName() != null
                && !assignmentRepository.existsByAssignmentName(request.getAssignmentName())) {
            assignment.setAssignmentName(request.getAssignmentName());
        }

        if (request.getAssignmentStatus() != null) {
            try {
                AssignmentStatus status = AssignmentStatus.valueOf(request.getAssignmentStatus());
                assignment.setAssignmentStatus(status);
            } catch (IllegalArgumentException e) {
                throw new AppException(ErrorCode.INVALID_ASSIGNMENT_STATUS);
            }
        }
        assignment.setUpdatedAt(LocalDate.now());

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

        assignment.setStatus(Status.DELETED);
        assignmentRepository.save(assignment);

        // Log action
        logService.log(
                "REMOVE_ASSIGNMENT",
                "ASSIGNMENT",
                assignment.getAssignmentId(),
                "Assignment removed: " + assignment.getAssignmentName());
    }
}
