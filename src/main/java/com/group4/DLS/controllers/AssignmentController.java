package com.group4.DLS.controllers;

import com.group4.DLS.domain.dto.request.AssignmentCreateRequest;
import com.group4.DLS.domain.dto.request.AssignmentUpdateRequest;
import com.group4.DLS.domain.dto.response.ApiResponse;
import com.group4.DLS.domain.dto.response.AssignmentResponse;
import com.group4.DLS.domain.dto.response.GuidelineResponse;
import com.group4.DLS.domain.entity.Assignment;
import com.group4.DLS.services.AssignmentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/assignments")
@RequiredArgsConstructor
@Tag(name = "Assignments", description = "Assignment management endpoints")
@SecurityRequirement(name = "Bearer Authentication")
public class AssignmentController {

    private final AssignmentService assignmentService;

        // 1 Get all assignments
        @GetMapping
        public ApiResponse<List<AssignmentResponse>> getAllAssignments() {
                ApiResponse<List<AssignmentResponse>> response = new ApiResponse<>();

                response.setCode(200);
                response.setData(assignmentService.getAllAssignments());
                response.setMessage("Get all assignment successfully");

                return response;
        }

    // 2 Create assignment
    @PostMapping("/projects/{projectId}/datasets/{datasetId}")
    @Operation(
        summary = "Create new assignment",
        description = "Create a new assignment linking a project with a dataset")
    public ApiResponse<AssignmentResponse> createAssignment(
            @PathVariable String projectId,
            @PathVariable String datasetId,
            @RequestBody AssignmentCreateRequest request) {
        return ApiResponse.<AssignmentResponse>builder()
                .code(201)
                .message("Create assignment successfully")
                .data(assignmentService.createAssignment(projectId, datasetId, request))
                .build();
    }

    // 3️ Update assignment (name + status)
    @PutMapping("/{assignmentId}")
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

    // 4️ Delete assignment
    @DeleteMapping("/{assignmentId}")
    @Operation(
        summary = "Delete assignment",
        description = "Delete an assignment by its ID")
    public ApiResponse<Void> deleteAssignment(
            @PathVariable String assignmentId) {
        assignmentService.deleteAssignment(assignmentId);

        return ApiResponse.<Void>builder()
                .code(200)
                .message("Delete assignment successfully")
                .build();
    }
}
