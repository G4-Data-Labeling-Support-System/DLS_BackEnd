package com.group4.DLS.controllers;

import com.group4.DLS.domain.dto.request.DatasetCreationRequest;
import com.group4.DLS.domain.dto.request.DatasetUpdateRequest;
import com.group4.DLS.domain.dto.response.DatasetResponse;
import com.group4.DLS.services.DatasetService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/datasets")
@RequiredArgsConstructor
@Tag(name = "Datasets", description = "Dataset management endpoints")
@SecurityRequirement(name = "Bearer Authentication")
public class DatasetController {

    private final DatasetService datasetService;

    @PostMapping
    @Operation(
        summary = "Create new dataset",
        description = "Create a new dataset for a project"
    )
    public ResponseEntity<DatasetResponse> create(
            @RequestBody DatasetCreationRequest request) {
        return ResponseEntity.ok(datasetService.createDataset(request));
    }

    @GetMapping("/project/{projectId}")
    @Operation(
        summary = "Get datasets by project",
        description = "Retrieve all datasets belonging to a specific project"
    )
    public List<DatasetResponse> getAllByProject(
            @PathVariable String projectId) {
        return datasetService.getAllDatasetByProject(projectId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DatasetResponse> update(
            @PathVariable String id,
            @RequestBody DatasetUpdateRequest request) {
        return ResponseEntity.ok(datasetService.updateDataset(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete dataset",
        description = "Delete a dataset by its ID"
    )
    public ResponseEntity<Void> delete(@PathVariable String id) {
        datasetService.deleteDataset(id);
        return ResponseEntity.noContent().build();
    }
}
