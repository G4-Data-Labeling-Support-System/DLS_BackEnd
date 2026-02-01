package com.group4.DLS.controller;

import com.group4.DLS.domain.dto.request.AssignmentCreateRequest;
import com.group4.DLS.domain.dto.request.AssignmentUpdateRequest;
import com.group4.DLS.domain.dto.response.ApiResponse;
import com.group4.DLS.domain.dto.response.AssignmentResponse;
import com.group4.DLS.domain.entity.Assignment;
import com.group4.DLS.service.AssignmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/assignments")
@RequiredArgsConstructor
public class AssignmentController {

    private final AssignmentService assignmentService;

    // 1 Get all assignments
    @GetMapping
    public ApiResponse<List<Assignment>> getAllAssignments() {
        return ApiResponse.<List<Assignment>>builder()
                .code(200)
                .message("Get all assignments successfully")
                .data(assignmentService.getAllAssignments())
                .build();
    }

    // 2 Create assignment
    @PostMapping("/projects/{projectId}/datasets/{datasetId}")
    public ApiResponse<AssignmentResponse> createAssignment(
            @PathVariable String projectId,
            @PathVariable String datasetId,
            @RequestBody AssignmentCreateRequest request
    ) {
        return ApiResponse.<AssignmentResponse>builder()
                .code(201)
                .message("Create assignment successfully")
                .data(assignmentService.createAssignment(projectId, datasetId, request))
                .build();
    }

    // 3️ Update assignment (name + status)
    @PutMapping("/{assignmentId}")
    public ApiResponse<AssignmentResponse> updateAssignment(
            @PathVariable String assignmentId,
            @RequestBody AssignmentUpdateRequest request
    ) {
        return ApiResponse.<AssignmentResponse>builder()
                .code(200)
                .message("Update assignment successfully")
                .data(assignmentService.updateAssignment(assignmentId, request))
                .build();
    }

    // 4️ Delete assignment
    @DeleteMapping("/{assignmentId}")
    public ApiResponse<Void> deleteAssignment(
            @PathVariable String assignmentId
    ) {
        assignmentService.deleteAssignment(assignmentId);

        return ApiResponse.<Void>builder()
                .code(200)
                .message("Delete assignment successfully")
                .build();
    }
}
