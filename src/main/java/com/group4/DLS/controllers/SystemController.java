package com.group4.DLS.controllers;

import com.group4.DLS.domain.dto.response.ApiResponse;
import com.group4.DLS.domain.dto.response.SeaweedClusterStatusResponse;
import com.group4.DLS.domain.entity.FileSizeUtil;
import com.group4.DLS.services.SeaweedMonitorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/seaweedfs_system")
@RequiredArgsConstructor
public class SystemController {
    private final SeaweedMonitorService monitorService;

    @GetMapping("/storage-status")
    //@PreAuthorize("hasRole('ADMIN')")
     public ApiResponse<SeaweedClusterStatusResponse> getStorageStatus() {
            ApiResponse<SeaweedClusterStatusResponse> response = new ApiResponse<>();

            System.out.println("API CALLED");
            response.setCode(200);
            response.setData(monitorService.getClusterStatus());
            response.setMessage("Connect successfully");

            return response;
    }

}
