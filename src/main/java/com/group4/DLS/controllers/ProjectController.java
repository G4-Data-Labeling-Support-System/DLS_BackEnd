package com.group4.DLS.controllers;

import com.group4.DLS.domain.dto.request.ProjectCreationRequest;
import com.group4.DLS.domain.dto.request.ProjectStatusUpdateRequest;
import com.group4.DLS.domain.dto.request.ProjectUpdateRequest;
import com.group4.DLS.domain.dto.response.ApiResponse;
import com.group4.DLS.domain.dto.response.ProjectMemberResponse;
import com.group4.DLS.domain.dto.response.ProjectResponse;
import com.group4.DLS.domain.dto.response.UserResponse;
import com.group4.DLS.services.ProjectMemberService;
import com.group4.DLS.services.ProjectService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
@Tag(name = "Projects", description = "Project management endpoints")
@SecurityRequirement(name = "Bearer Authentication")
public class ProjectController {

    private final ProjectService projectService;
    private final ProjectMemberService projectMemberService;

    /*
    * ================
    * Create new project
    * ===============
    */
    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(
        summary = "Create new project",
        description = "Create a new data labeling project. Requires MANAGER role."
    )
    public ApiResponse<ProjectResponse> createApiResponse(
            @RequestBody ProjectCreationRequest request) {

        return ApiResponse.<ProjectResponse>builder()
                .code(200)
                .message("Project created successfully")
                .data(projectService.createProject(request))
                .build();
    }

    /*
    * ================
    * Update a project
    * ===============
    */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ApiResponse<ProjectResponse> updateApiResponse(@PathVariable String id, @RequestBody ProjectUpdateRequest request) {
        return ApiResponse.<ProjectResponse>builder()
                .code(200)
                .message("Project updated successfully")
                .data(projectService.updateProject(id, request))
                .build();
    }

    /*
    * ================
    * Update a project status
    * ===============
    */
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('MANAGER')")
    public ApiResponse<ProjectResponse> updateProjectStatusApiResponse(@PathVariable String id, @RequestBody ProjectStatusUpdateRequest request) {
        return ApiResponse.<ProjectResponse>builder()
                .code(200)
                .message("Project status updated successfully")
                .data(projectService.updateProjectStatus(id, request))
                .build();
    }

    /*
    * ================
    * Remove a project
    * ===============
    */
    @PatchMapping("/{id}/remove")
    @PreAuthorize("hasRole('MANAGER')")
    public ApiResponse<Void> deleteApiResponse(@PathVariable String id) {
        projectService.deleteProject(id);

        return ApiResponse.<Void>builder()
                .code(200)
                .message("Project remove successfully")
                .build();
    }

    /*
    * ================
    * Get project by id
    * ===============
    */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN', 'ANNOTATOR', 'REVIEWER')")
    @Operation(
        summary = "Get project by ID",
        description = "Retrieve detailed information about a specific project"
    )
    public ApiResponse<ProjectResponse> getByIdApiResponse(@PathVariable String id) {
        return ApiResponse.<ProjectResponse>builder()
                .code(200)
                .message("Get project successfully")
                .data(projectService.getProjectById(id))
                .build();
    }

    /*
    * ================
    * List all projects
    * ===============
    */
    @GetMapping
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN', 'ANNOTATOR', 'REVIEWER')")
    @Operation(
        summary = "List all projects",
        description = "Retrieve a list of all data labeling projects"
    )
    public ApiResponse<List<ProjectResponse>> getAllProjectsApiResponse() {
        return ApiResponse.<List<ProjectResponse>>builder()
                .code(200)
                .message("Get all projects successfully")
                .data(projectService.getAllProjects())
                .build();
    }

    //get all member of project
    @GetMapping("/{projectId}/members")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(
        summary = "Get project members",
        description = "Retrieve all members associated with a specific project")
    public ApiResponse<List<ProjectMemberResponse>> getProjectMembersApiResponse(@PathVariable String projectId){
        return ApiResponse.<List<ProjectMemberResponse>>builder()
                .code(200)
                .message("Get project members successfully")
                .data(projectMemberService.getMembersByProjectId(projectId))
                .build();
    }
}
