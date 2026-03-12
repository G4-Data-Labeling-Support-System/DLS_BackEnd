package com.group4.DLS.controllers;


import com.group4.DLS.domain.dto.response.ApiResponse;
import com.group4.DLS.domain.dto.response.DataItemResponse;
import com.group4.DLS.domain.dto.response.TaskResponse;
import com.group4.DLS.domain.entity.Dataitem;
import com.group4.DLS.services.TaskDataItemService;
import com.group4.DLS.services.TaskService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Tasks", description = "Task management endpoints")
@SecurityRequirement(name = "Bearer Authentication")
public class TaskController {

    TaskService taskService;
    TaskDataItemService taskDataItemService;

    @GetMapping("/assignments/{assignmentId}")
    @PreAuthorize("hasAnyRole('MANAGER','REVIEWER','ANNOTATOR','ADMIN')") // Adjust roles as needed
    public ApiResponse<List<TaskResponse>> getTaskByAssignmentId(@PathVariable  String assignmentId){
        ApiResponse<List<TaskResponse>> response = new ApiResponse<>();
        response.setCode(200);
        response.setData(taskService.getTasksByAssignmentId(assignmentId));
        response.setMessage("Tasks retrieved successfully");
        return response;
    }

    @GetMapping("/{taskId}")
    public ApiResponse<List<DataItemResponse>> getTaskById(@PathVariable String taskId) {
        ApiResponse<List<DataItemResponse>> response = new ApiResponse<>();
        response.setCode(200);
        response.setData(taskDataItemService.getDataitemsByTaskId(taskId));
        response.setMessage("Task retrieved successfully");
        return response;
    }
}
