package com.group4.DLS.controllers;

import com.group4.DLS.domain.dto.response.ApiResponse;
import com.group4.DLS.domain.dto.response.DataItemResponse;
import com.group4.DLS.services.DataitemService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dataitems")
@RequiredArgsConstructor
public class DataItemController {

    private final DataitemService dataitemService;

    @GetMapping("/datasets/{id}")
    public ApiResponse<List<DataItemResponse>> getAllDataset(@PathVariable String id) {
        ApiResponse<List<DataItemResponse>> response = new ApiResponse<>();
        response.setCode(200);
        response.setData(dataitemService.getAllDataitemForDataset(id));
        response.setMessage("Get all dataitem for dataset successfully");
        return response;
    }

    @GetMapping("/{id}")
    public  ApiResponse<DataItemResponse> getDataItemById(@PathVariable String id) {
        ApiResponse<DataItemResponse> response = new ApiResponse<>();
        response.setCode(200);
        response.setData(dataitemService.getDataitemById(id));
        response.setMessage("Get dataitem by id successfully");
        return response;
    }
}