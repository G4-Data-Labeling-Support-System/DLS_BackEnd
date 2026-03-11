package com.group4.DLS.controllers;

import com.group4.DLS.domain.dto.request.DatasetCreationRequest;
import com.group4.DLS.domain.dto.request.DatasetUpdateRequest;
import com.group4.DLS.domain.dto.response.ApiResponse;
import com.group4.DLS.domain.dto.response.DatasetResponse;
import com.group4.DLS.services.DatasetService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/datasets")
@RequiredArgsConstructor
@Tag(name = "Datasets", description = "Dataset management endpoints")
@SecurityRequirement(name = "Bearer Authentication")
public class DatasetController {

    private final DatasetService datasetService;

    /*
     * ==============================
     * Get all dataset
     * ==============================
     */
    @GetMapping
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(
            summary = "Get all datasets",
            description = "Retrieve all datasets"
    )
    public List<DatasetResponse> getAllDataset() {
        return datasetService.getAllDatasets();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(
            summary = "Get dataset by id",
            description = "Retrieve dataset"
    )
    public DatasetResponse getDatasetById(@PathVariable String id) {
        return datasetService.getDatasetById(id);
    }

    /*
    * ==============================
    * Get datasets for target project
    * ==============================
    */
    @GetMapping("/project/{projectId}")
    @PreAuthorize("hasAnyRole('MANAGER','ANNOTATOR')")
    @Operation(
        summary = "Get datasets by project",
        description = "Retrieve all datasets belonging to a specific project"
    )
    public List<DatasetResponse> getAllByProjectDatasetResponses(
            @PathVariable String projectId) {
        return datasetService.getAllDatasetForProject(projectId);
    }

    /*
    * ==================
    * Create new dataset
    * ==================
    */
    @PostMapping (value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(
        summary = "Create new dataset",
        description = "Create a new dataset for a project"
    )
    public ApiResponse<DatasetResponse> createDatasetApiResponse(
            @ModelAttribute DatasetCreationRequest request) throws IOException {
        return ApiResponse.<DatasetResponse>builder()
                .code(200)
                .message("Dataset created successfully")
                .data(datasetService.createDataset(request))
                .build();
    }

    /*
    * ================
    * Update a dataset
    * ================
    */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(
        summary = "Update current dataset",
        description = "Update dataset"
    )
    public ApiResponse<DatasetResponse> update(@PathVariable String id, @RequestBody DatasetUpdateRequest request) {
        ApiResponse<DatasetResponse> response = new ApiResponse<>();

        response.setCode(200);
        response.setData(datasetService.updateDatasetResponse(id, request));
        response.setMessage("Dataset updated successfully");

        return response;
    }

    /*
    * ================
    * Remove a dataset
    * ================
    */
    // @DeleteMapping("/{id}")
    // @Operation(
    //     summary = "Delete dataset",
    //     description = "Delete a dataset by its ID"
    // )
    // public ResponseEntity<Void> delete(@PathVariable String id) {
    //     datasetService.deleteDataset(id);
    //     return ResponseEntity.noContent().build();
    // }
}
