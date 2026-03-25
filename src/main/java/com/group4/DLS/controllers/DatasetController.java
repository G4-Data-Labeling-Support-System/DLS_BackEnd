package com.group4.DLS.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.web.multipart.MultipartFile;

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
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    @Operation(summary = "Get all datasets", description = "Retrieve all datasets")
    public List<DatasetResponse> getAllDataset() {
        return datasetService.getAllDatasets();
    }

    @GetMapping("/{datasetId}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    @Operation(summary = "Get dataset by dataset_id", description = "Retrieve dataset")
    public DatasetResponse getDatasetById(@PathVariable String datasetId) {
        return datasetService.getDatasetById(datasetId);
    }

    /*
     * ==============================
     * Get datasets for target project
     * ==============================
     */
    @GetMapping("/project/{projectId}")
    @PreAuthorize("hasAnyRole('MANAGER','ANNOTATOR', 'ADMIN')")
    @Operation(summary = "Get datasets by project", description = "Retrieve all datasets belonging to a specific project")
    public List<DatasetResponse> getAllByProjectDatasetResponses(
            @PathVariable String projectId) {
        return datasetService.getAllDatasetForProject(projectId);
    }

    /*
     * ==================
     * Create new dataset
     * ==================
     */
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Create new dataset", description = "Create a new dataset for a project")
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
        @PutMapping(value = "/{datasetId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        @PreAuthorize("hasRole('MANAGER')")
        public ApiResponse<DatasetResponse> update(
                @PathVariable String datasetId,
                @RequestPart("request") String requestJson, // Dùng String + parse JSON
                @RequestParam(required = false) List<MultipartFile> files
        ) throws IOException {
            ObjectMapper mapper = new ObjectMapper();
            DatasetUpdateRequest request = mapper.readValue(requestJson, DatasetUpdateRequest.class);

            ApiResponse<DatasetResponse> response = new ApiResponse<>();

            response.setCode(200);
            response.setData(datasetService.updateDataset(datasetId, request, files));
            response.setMessage("Dataset updated successfully");

            return response;
        }

    /*
     * ================
     * Remove a dataset
     * ================
     */
    @DeleteMapping("/remove/{datasetId}")
    @Operation(summary = "Delete dataset", description = "Delete a dataset by its ID")
    public ApiResponse<DatasetResponse> delete(@PathVariable String datasetId) {
        ApiResponse<DatasetResponse> response = new ApiResponse<>();
        
        response.setCode(200);
        response.setData(datasetService.deleteDataset(datasetId));
        response.setMessage("Dataset deleted successfully");
        
        return response;
    }


    //get datasets not have assignment of project
    @GetMapping("/notassignment/project/{projectId}")
    @PreAuthorize("hasAnyRole('MANAGER','ANNOTATOR', 'ADMIN')")
    @Operation(summary = "Get datasets not have assignment by project", description = "Retrieve all datasets belonging to a specific project")
    public ApiResponse<List<DatasetResponse>> getDatasetsByAssignmentIsNullAndProject(
            @PathVariable String projectId) {
        ApiResponse<List<DatasetResponse>> response = new ApiResponse<>();

        response.setCode(200);
        response.setData(datasetService.getDatasetsNotHaveAssignmentInProject(projectId));
        response.setMessage("Dataset not have assignment successfully");

        return response;
    }
}
