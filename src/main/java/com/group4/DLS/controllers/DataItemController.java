package com.group4.DLS.controllers;

import com.group4.DLS.domain.dto.response.DataItemResponse;
import com.group4.DLS.services.DataitemService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dataitems")
@RequiredArgsConstructor
public class DataItemController {

    @Autowired
    private final DataitemService dataitemService;

    @GetMapping("/datasets/{id}")
    public List<DataItemResponse> getAllDataset(@PathVariable String id) {
        return dataitemService.getAllDataitemForDataset(id);
    }

    @GetMapping("/{id}")
    public DataItemResponse getDataItemById(@PathVariable String id) {
        return dataitemService.getDataitemById(id);
    }
}