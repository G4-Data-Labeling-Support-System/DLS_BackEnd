package com.group4.DLS.controller;

import com.group4.DLS.domain.dto.request.DatasetCreationRequest;
import com.group4.DLS.domain.dto.request.DatasetUpdateRequest;
import com.group4.DLS.domain.dto.response.ApiResponse;
import com.group4.DLS.domain.dto.response.DatasetResponse;
import com.group4.DLS.service.DatasetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/datasets")
@RequiredArgsConstructor
public class DatasetController {

    private final DatasetService datasetService;

    // ================= CREATE DATASET =================
    @PostMapping
    public ApiResponse<DatasetResponse> create(
            @RequestBody DatasetCreationRequest request) {

        return ApiResponse.<DatasetResponse>builder()
                .code(200)
                .message("Dataset created successfully")
                .data(datasetService.createDataset(request))
                .build();
    }

    // ================= GET DATASETS BY PROJECT =================
    @GetMapping("/project/{projectId}")
    public ApiResponse<List<DatasetResponse>> getAllByProject(
            @PathVariable String projectId) {

        return ApiResponse.<List<DatasetResponse>>builder()
                .code(200)
                .message("Get datasets by project successfully")
                .data(datasetService.getAllDatasetByProject(projectId))
                .build();
    }

    // ================= UPDATE DATASET =================
    @PutMapping("/{id}")
    public ApiResponse<DatasetResponse> update(
            @PathVariable String id,
            @RequestBody DatasetUpdateRequest request) {

        return ApiResponse.<DatasetResponse>builder()
                .code(200)
                .message("Dataset updated successfully")
                .data(datasetService.updateDataset(id, request))
                .build();
    }

    // ================= DELETE DATASET =================
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable String id) {

        datasetService.deleteDataset(id);

        return ApiResponse.<Void>builder()
                .code(200)
                .message("Dataset deleted successfully")
                .build();
    }
}
