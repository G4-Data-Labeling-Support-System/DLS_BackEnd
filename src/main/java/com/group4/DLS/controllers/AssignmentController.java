package com.group4.DLS.controllers;

import com.group4.DLS.domain.dto.request.AssignmentCreateRequest;
import com.group4.DLS.domain.dto.request.AssignmentDatasetChangeRequest;
import com.group4.DLS.domain.dto.request.AssignmentUpdateRequest;
import com.group4.DLS.domain.dto.request.ExportRequest;
import com.group4.DLS.domain.dto.response.ApiResponse;
import com.group4.DLS.domain.dto.response.AssignmentResponse;
import com.group4.DLS.domain.dto.response.DatasetResponse;
import com.group4.DLS.domain.dto.response.LabelResponse;
import com.group4.DLS.services.AssignmentService;

import com.group4.DLS.services.ExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;

@RestController
@RequestMapping("/api/v1/assignments")
@RequiredArgsConstructor
@Tag(name = "Assignments", description = "Assignment management endpoints")
@SecurityRequirement(name = "Bearer Authentication")
public class AssignmentController {

    private final AssignmentService assignmentService;
    private final ExportService exportService;


    //============= Export===========
    @PostMapping("/{id}/export")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Resource> export(
            @PathVariable("id") String assignmentId,
            @RequestBody ExportRequest request) throws Exception {

        File file = exportService.export(assignmentId, request.getExportFormat());

        FileSystemResource resource = new FileSystemResource(file);

        ResponseEntity<Resource> response = ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);

        return response;
    }

    // ================= GET ASSIGNMENT BY ASSIGNMENT_ID =================
    @GetMapping("/{assignmentId}")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN','ANNOTATOR', 'REVIEWER')") // Allow access to managers, admins, and annotators
    public ApiResponse<AssignmentResponse> getAssignmentById( @PathVariable String assignmentId) {
        ApiResponse<AssignmentResponse> response = new ApiResponse<>();
        response.setCode(200);
        response.setData(assignmentService.getAssignmentById(assignmentId));
        response.setMessage("Get assignment by id successfully");
        return response;
    }

    // ================= GET ASSIGNMENT BY ANNOTATOR_ID =================
    @GetMapping("/annotators/{annotatorId}")
    @PreAuthorize("hasAnyRole('ANNOTATOR', 'ADMIN')")
    public ApiResponse<List<AssignmentResponse>> getAssignmentsForAnnotator( @PathVariable String annotatorId) {
        ApiResponse<List<AssignmentResponse>> response = new ApiResponse<>();
        response.setCode(200);
        response.setData(assignmentService.getAssignmentForAnnotator(annotatorId));
        response.setMessage("Get all assignment for annotator successfully");
        return response;
    }

    @GetMapping("/reviewers/{reviewerId}")
    @PreAuthorize("hasAnyRole('REVIEWER', 'ADMIN')")
    public ApiResponse<List<AssignmentResponse>> getAssignmentsForReviewer( @PathVariable String reviewerId) {
        ApiResponse<List<AssignmentResponse>> response = new ApiResponse<>();
        response.setCode(200);
        response.setData(assignmentService.getAssignmentForReviewer(reviewerId));
        response.setMessage("Get all assignment for annotator successfully");
        return response;
    }

    // ================= GET ASSIGNMENT FOR PRORJECT =================
    @GetMapping("/projects/{projectId}")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN','ANNOTATOR', 'REVIEWER')") // Allow access to managers, admins, and annotators
    public ApiResponse<List<AssignmentResponse>> getAssignmentsForProject( @PathVariable String projectId) {
        ApiResponse<List<AssignmentResponse>> response = new ApiResponse<>();

        response.setCode(200);
        response.setData(assignmentService.getAssignmentForProject(projectId));
        response.setMessage("Get all assignment for project successfully");
        return response;
    }

    // ================= GET ALL ASSIGNMENT =================
    @GetMapping
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN','ANNOTATOR', 'REVIEWER')") // Allow access to managers, admins, and annotators
    public ApiResponse<List<AssignmentResponse>> getAllAssignments() {
        ApiResponse<List<AssignmentResponse>> response = new ApiResponse<>();

        response.setCode(200);
        response.setData(assignmentService.getAllAssignments());
        response.setMessage("Get all assignment successfully");
        return response;
    }

    // ================= CREATE NEW ASSIGNMENT =================
    @PostMapping("/projects/{projectId}")
    @PreAuthorize("hasAnyRole('MANAGER')")
    @Operation(
        summary = "Create new assignment",
        description = "Create a new assignment linking a project with a dataset")
    public ApiResponse<AssignmentResponse> createAssignment(
            @PathVariable String projectId,
            @RequestBody AssignmentCreateRequest request) {
        return ApiResponse.<AssignmentResponse>builder()
                .code(201)
                .message("Create assignment successfully")
                .data(assignmentService.createAssignment(projectId, request))
                .build();
    }

    // ================= UPDATE ASSIGNMENT (NAME + STATUS) =================
    @PutMapping("/{assignmentId}")
    @PreAuthorize("hasAnyRole('MANAGER')")
    @Operation(
        summary = "Update assignment",
        description = "Update an existing assignment by its ID")
    public ApiResponse<AssignmentResponse> updateAssignment(
            @PathVariable String assignmentId,
            @RequestBody AssignmentUpdateRequest request) {
        return ApiResponse.<AssignmentResponse>builder()
                .code(200)
                .message("Update assignment successfully")
                .data(assignmentService.updateAssignment(assignmentId, request))
                .build();
    }

    // ================= CHANGE DATASET FOR CURRENT ASSIGNMENT =================
    @PutMapping("/change-dataset/assignment/{assignmentId}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ANNOTATOR', 'REVIEWER')")
    @Operation(
        summary = "Change Assignment dataset",
        description = "Change Assignment dataset")
    public ApiResponse<AssignmentResponse> changeAssignmentDatasetApiResponse (
            @PathVariable String assignmentId,
            @RequestBody AssignmentDatasetChangeRequest request) {
        return ApiResponse.<AssignmentResponse>builder()
                .code(200)
                .message("Update assignment successfully")
                .data(assignmentService.changeDatasetForCurrentAssignment(assignmentId, request))
                .build();
    }

    // ================= REMOVE ASSIGNMENT =================
    @PatchMapping("/remove/{assignmentId}")
    @PreAuthorize("hasAnyRole('MANAGER')")
    @Operation(
        summary = "Delete assignment",
        description = "Delete an assignment by its ID")
    public ApiResponse<Void> deleteAssignment(
            @PathVariable String assignmentId) {
        assignmentService.removeAssignment(assignmentId);

        return ApiResponse.<Void>builder()
                .code(200)
                .message("Remove assignment successfully")
                .build();
    }

    // ================= GET LABELS BY ASSIGNMENT_ID =================
    @GetMapping("/{assignmentId}/labels")
    @PreAuthorize("hasAnyRole('MANAGER','ANNOTATOR','REVIEWER', 'ADMIN')")
    @Operation(
        summary = "Get labels for assignment",
        description = "Retrieve all labels associated with a specific assignment")
    public ApiResponse<List<LabelResponse>> getLabelsForAssignment(
            @PathVariable String assignmentId) {
        return ApiResponse.<List<LabelResponse>>builder()
                .code(200)
                .message("Get labels for assignment successfully")
                .data(assignmentService.getLabelsForAssignment(assignmentId))
                .build();
    }

    // ================= GET DATASET BY ASSIGNMENT_ID =================
    @GetMapping("/{assignmentId}/dataset")
    @PreAuthorize("hasAnyRole('MANAGER','ANNOTATOR','REVIEWER', 'ADMIN')")
    @Operation(
            summary = "Get dataset for assignment",
            description = "Retrieve all dataset associated with a specific assignment")
    public ApiResponse<DatasetResponse> getDatasetForAssignment(
            @PathVariable String assignmentId) {
        return ApiResponse.<DatasetResponse>builder()
                .code(200)
                .message("Get dataset for assignment successfully")
                .data(assignmentService.getDatasetByAssignmentId(assignmentId))
                .build();
    }
}
