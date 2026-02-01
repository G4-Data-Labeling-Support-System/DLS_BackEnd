package com.group4.DLS.controller;

import com.group4.DLS.domain.dto.request.ProjectCreationRequest;
import com.group4.DLS.domain.dto.request.ProjectUpdateRequest;
import com.group4.DLS.domain.dto.response.ApiResponse;
import com.group4.DLS.domain.dto.response.ProjectResponse;
import com.group4.DLS.service.ProjectService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("hasRole('MANAGER')")
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    // CREATE PROJECT
    @PostMapping
    public ApiResponse<ProjectResponse> create(
            @RequestBody ProjectCreationRequest request) {

        return ApiResponse.<ProjectResponse>builder()
                .code(200)
                .message("Project created successfully")
                .data(projectService.createProject(request))
                .build();
    }

    // UPDATE PROJECT
    @PutMapping("/{id}")
    public ApiResponse<ProjectResponse> update(
            @PathVariable String id,
            @RequestBody ProjectUpdateRequest request) {

        return ApiResponse.<ProjectResponse>builder()
                .code(200)
                .message("Project updated successfully")
                .data(projectService.updateProject(id, request))
                .build();
    }

    // DELETE PROJECT (soft / logic delete)
    @PatchMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable String id) {
        projectService.deleteProject(id);

        return ApiResponse.<Void>builder()
                .code(200)
                .message("Project deleted successfully")
                .build();
    }

    // GET PROJECT BY ID
    @GetMapping("/{id}")
    public ApiResponse<ProjectResponse> getById(@PathVariable String id) {

        return ApiResponse.<ProjectResponse>builder()
                .code(200)
                .message("Get project successfully")
                .data(projectService.getProjectById(id))
                .build();
    }

    // LIST ALL PROJECTS
    @GetMapping
    public ApiResponse<List<ProjectResponse>> getAllProjects() {

        return ApiResponse.<List<ProjectResponse>>builder()
                .code(200)
                .message("Get all projects successfully")
                .data(projectService.getAllProjects())
                .build();
    }
}
