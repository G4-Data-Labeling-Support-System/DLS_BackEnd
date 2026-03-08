package com.group4.DLS.controllers;

import com.group4.DLS.domain.dto.request.LabelCreationRequest;
import com.group4.DLS.domain.dto.request.LabelUpdateRequest;
import com.group4.DLS.domain.dto.response.ApiResponse;
import com.group4.DLS.domain.dto.response.LabelResponse;
import com.group4.DLS.services.LabelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Labels", description = "Label management endpoints")
@SecurityRequirement(name = "Bearer Authentication")
public class LabelController {

    private final LabelService labelService;

    /*
     * ==============================
     * GET ALL LABELS BY DATASET
     * ==============================
     */
    @GetMapping("/api/v1/datasets/{datasetId}/labels")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    @Operation(
            summary = "Get labels by dataset",
            description = "Retrieve all labels belonging to a specific dataset"
    )
    public List<LabelResponse> getAllByDataset(
            @PathVariable String datasetId) {

        return labelService.getAllByDataset(datasetId);
    }

    /*
     * ==============================
     * CREATE LABEL FOR DATASET
     * ==============================
     */
    @PostMapping("/api/v1/datasets/{datasetId}/labels")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    @Operation(
            summary = "Create new label",
            description = "Create a new label for a dataset"
    )
    public ApiResponse<LabelResponse> createLabel(
            @PathVariable String datasetId,
            @RequestBody LabelCreationRequest request) {

        return ApiResponse.<LabelResponse>builder()
                .code(200)
                .message("Label created successfully")
                .data(labelService.create(datasetId, request))
                .build();
    }

    /*
     * ==============================
     * GET ALL LABELS
     * ==============================
     */
    @GetMapping("/api/v1/labels")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    @Operation(
            summary = "Get all labels",
            description = "Retrieve all labels in system"
    )
    public List<LabelResponse> getAllLabels() {

        return labelService.getAllLabels();
    }

    /*
     * ==============================
     * GET LABEL BY ID
     * ==============================
     */
    @GetMapping("/api/v1/labels/{labelId}")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    @Operation(
            summary = "Get label by ID",
            description = "Retrieve a specific label by its ID"
    )
    public LabelResponse getById(@PathVariable String labelId) {

        return labelService.getById(labelId);
    }

    /*
     * ==============================
     * UPDATE LABEL
     * ==============================
     */
    @PutMapping("/api/v1/labels/{labelId}")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    @Operation(
            summary = "Update label",
            description = "Update a label"
    )
    public ApiResponse<LabelResponse> update(
            @PathVariable String labelId,
            @RequestBody LabelUpdateRequest request) {

        return ApiResponse.<LabelResponse>builder()
                .code(200)
                .message("Label updated successfully")
                .data(labelService.update(labelId, request))
                .build();
    }

    /*
     * ==============================
     * DELETE LABEL
     * ==============================
     */
    @DeleteMapping("/api/v1/labels/{labelId}")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    @Operation(
            summary = "Delete label",
            description = "Delete a label by its ID"
    )
    public ApiResponse<Void> delete(@PathVariable String labelId) {

        labelService.delete(labelId);

        return ApiResponse.<Void>builder()
                .code(200)
                .message("Label deleted successfully")
                .build();
    }

}