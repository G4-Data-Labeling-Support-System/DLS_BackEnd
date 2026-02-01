package com.group4.DLS.controllers;

import com.group4.DLS.domain.dto.request.DatasetCreationRequest;
import com.group4.DLS.domain.dto.request.DatasetUpdateRequest;
import com.group4.DLS.domain.dto.response.DatasetResponse;
import com.group4.DLS.services.DatasetService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/datasets")
@RequiredArgsConstructor
public class DatasetController {

    private final DatasetService datasetService;

    @PostMapping
    public ResponseEntity<DatasetResponse> create(
            @RequestBody DatasetCreationRequest request) {
        return ResponseEntity.ok(datasetService.createDataset(request));
    }

    @GetMapping("/project/{projectId}")
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
    public ResponseEntity<Void> delete(@PathVariable String id) {
        datasetService.deleteDataset(id);
        return ResponseEntity.noContent().build();
    }
}
