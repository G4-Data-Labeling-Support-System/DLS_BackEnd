package com.group4.DLS.controllers;

import com.group4.DLS.domain.dto.response.ApiResponse;
import com.group4.DLS.domain.dto.response.DataItemResponse;
import com.group4.DLS.services.DataitemService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dataitems")
@RequiredArgsConstructor
@Tag(name = "DataItems", description = "DataItems management endpoints")
@SecurityRequirement(name = "Bearer Authentication")
public class DataItemController {

    private final DataitemService dataitemService;

    @GetMapping("/datasets/{id}")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN','ANNOTATOR')")
    public ApiResponse<List<DataItemResponse>> getAllDataset(@PathVariable String id) {
        ApiResponse<List<DataItemResponse>> response = new ApiResponse<>();
        response.setCode(200);
        response.setData(dataitemService.getAllDataitemForDataset(id));
        response.setMessage("Get all dataitem for dataset successfully");
        return response;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN','ANNOTATOR')")
    public  ApiResponse<DataItemResponse> getDataItemById(@PathVariable String id) {
        ApiResponse<DataItemResponse> response = new ApiResponse<>();
        response.setCode(200);
        response.setData(dataitemService.getDataitemById(id));
        response.setMessage("Get dataitem by id successfully");
        return response;
    }

    @DeleteMapping("/{id}/delete")
    @PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
    public ApiResponse<Void> deleteDataItem(@PathVariable String id) {
        ApiResponse<Void> response = new ApiResponse<>();
        dataitemService.deleteDataitem(id);
        response.setCode(200);
        response.setMessage("Data item deleted successfully");
        return response;
    }
}