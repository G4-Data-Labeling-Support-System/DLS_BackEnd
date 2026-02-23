package com.group4.DLS.controllers;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.group4.DLS.domain.dto.response.ApiResponse;
import com.group4.DLS.domain.entity.ActivityLog;
import com.group4.DLS.services.ActivityLogService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/api/v1/logs")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Activity Logs", description = "Logs management endpoints")
@SecurityRequirement(name = "Bearer Authentication")
public class ActivityLogController {
    ActivityLogService activityLogService;

    /*
    * ================
    * Get All Logs
    * ===============
    */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(
        summary = "Get all logs",
        description = "Retrieve a list of all activity logs in the system"
    )
    public ApiResponse<List<ActivityLog>> getAllLogs() {
        ApiResponse<List<ActivityLog>> response = new ApiResponse<>();
        
        response.setCode(200);
        response.setData(activityLogService.getAllLogs());
        response.setMessage("Logs retrieved successfully");

        return response;
    }
}
