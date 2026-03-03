package com.group4.DLS.services;

import com.group4.DLS.domain.dto.request.AssignmentCreateRequest;
import com.group4.DLS.domain.dto.request.AssignmentUpdateRequest;
import com.group4.DLS.domain.dto.request.AuthRequest;
import com.group4.DLS.domain.dto.response.AssignmentResponse;
import com.group4.DLS.domain.entity.Assignment;
import com.group4.DLS.domain.entity.Dataset;
import com.group4.DLS.domain.entity.Project;
import com.group4.DLS.domain.entity.User;
import com.group4.DLS.domain.entity.enums.AssignmentStatus;
import com.group4.DLS.domain.entity.enums.Status;
import com.group4.DLS.exceptions.AppException;
import com.group4.DLS.exceptions.enums.ErrorCode;
import com.group4.DLS.mappers.AssignmentMapper;
import com.group4.DLS.repositories.AssignmentRepository;
import com.group4.DLS.repositories.DatasetRepository;
import com.group4.DLS.repositories.ProjectRepository;
import com.group4.DLS.repositories.UserRepository;
import com.group4.DLS.security.CurrentUserProvider;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    User manager = userRepository.findById(request.getAssignedBy())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));


        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUND));

        Dataset dataset = datasetRepository.findById(datasetId)
                .orElseThrow(() -> new AppException(ErrorCode.DATASET_NOT_FOUND));

        User assignedTo = userRepository.findById(request.getAssignedTo())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Assignment assignment = new Assignment();
        assignment.setAssignmentName(request.getAssignmentName());
        assignment.setDescription(request.getDescription());
        assignment.setDueDate(LocalDateTime.now());

        assignment.setProject(project);
        assignment.setDataset(dataset);
        dataset.setAssignment(assignment);


        assignment.setAssignedBy(manager);
        assignment.setAssignedTo(assignedTo);
      
        datasetRepository.save(dataset);
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
