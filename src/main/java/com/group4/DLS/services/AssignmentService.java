package com.group4.DLS.services;

import com.group4.DLS.domain.dto.request.AssignmentCreateRequest;
import com.group4.DLS.domain.dto.request.AssignmentUpdateRequest;
import com.group4.DLS.domain.dto.response.AssignmentResponse;
import com.group4.DLS.domain.entity.Assignment;
import com.group4.DLS.domain.entity.Dataset;
import com.group4.DLS.domain.entity.Project;
import com.group4.DLS.domain.entity.enums.AssignmentStatus;
import com.group4.DLS.exceptions.AppException;
import com.group4.DLS.exceptions.enums.ErrorCode;
import com.group4.DLS.mappers.AssignmentMapper;
import com.group4.DLS.repositories.AssignmentRepository;
import com.group4.DLS.repositories.DatasetRepository;
import com.group4.DLS.repositories.ProjectRepository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

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

    public List<Assignment> getAllAssignments() {
        return assignmentRepository.findAll();
    }

    public AssignmentResponse createAssignment(String projectId, String datasetId, AssignmentCreateRequest request) {
        if (assignmentRepository.existsByAssignmentName(request.getAssignmentName())) {
            throw new AppException(ErrorCode.ASSIGNMENT_EXISTS);
        }
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new AppException(ErrorCode.PROJECT_NOT_FOUND));

        Dataset dataset = datasetRepository.findById(datasetId)
                .orElseThrow(() -> new AppException(ErrorCode.DATASET_NOT_FOUND));
        Assignment assignment = assignmentMapper.toAssignment(request);
        assignment.setProject(project);
        assignment.setDataset(dataset);

        assignmentRepository.save(assignment);

        // Log action
        logService.log(null);

        return assignmentMapper.toResponse(assignment);
    }

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
        return assignmentMapper.toResponse(assignment);
    }

    public void deleteAssignment(String assignmentId) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new AppException(ErrorCode.ASSIGNMENT_NOT_FOUND));

        assignment.setAssignmentStatus(AssignmentStatus.CANCELED);
        assignmentRepository.save(assignment);
    }

}
