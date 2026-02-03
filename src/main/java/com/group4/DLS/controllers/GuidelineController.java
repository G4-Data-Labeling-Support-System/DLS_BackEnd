package com.group4.DLS.controllers;

import com.group4.DLS.domain.dto.request.GuidelineCreateRequest;
import com.group4.DLS.domain.dto.response.ApiResponse;
import com.group4.DLS.domain.dto.response.GuidelineResponse;
import com.group4.DLS.domain.entity.Guideline;
import com.group4.DLS.services.GuidelineService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/guidelines")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Guidelines", description = "Guideline management endpoints")
@SecurityRequirement(name = "Bearer Authentication")
public class GuidelineController {

    GuidelineService guidelineService;

    /*
     * ======================
     * CREATE (version = 1)
     * ======================
     */
    @PostMapping("/project/{projectId}")
    @Operation(
        summary = "Create new guideline",
        description = "Create a new guideline for a project"
    )
    public ApiResponse<GuidelineResponse> createGuideline(
            @PathVariable String projectId,
            @RequestBody @Valid GuidelineCreateRequest request
    ) {
        ApiResponse<GuidelineResponse> response = new ApiResponse<>();
        response.setCode(200);
        response.setMessage("Guideline created successfully");
        response.setData(guidelineService.create(projectId, request));
        return response;
    }

    /*
     * ======================
     * UPDATE (versioning)
     * ======================
     */
    @PutMapping("/{guidelineId}")
    @Operation(
        summary = "Update guideline",
        description = "Update an existing guideline by its ID"
    )
    public ApiResponse<GuidelineResponse> updateGuideline(
            @PathVariable String guidelineId,
            @RequestBody @Valid GuidelineCreateRequest request
    ) {
        ApiResponse<GuidelineResponse> response = new ApiResponse<>();
        response.setCode(200);
        response.setMessage("Guideline updated successfully");
        response.setData(guidelineService.update(guidelineId, request));
        return response;
    }

    /*
     * ======================
     * GET all guidelines
     * ======================
     */
    @GetMapping("/project/{projectId}")
    @Operation(
        summary = "Get all guidelines by project",
        description = "Retrieve all guidelines associated with a specific project"
    )
    public ApiResponse<List<GuidelineResponse>> getAllGuidelinesByProject(
            @PathVariable String projectId
    ) {
        ApiResponse<List<GuidelineResponse>> response = new ApiResponse<>();
        response.setCode(200);
        response.setMessage("Guidelines retrieved successfully by project");
        response.setData(guidelineService.getAllByProject(projectId));
        return response;
    }


    @GetMapping
    public ApiResponse<List<GuidelineResponse>> getAllGuideline() {
        ApiResponse<List<GuidelineResponse>> response = new ApiResponse<>();

        response.setCode(200);
        response.setData(guidelineService.getAllGuideline());
        response.setMessage("GuideLines retrieved successfully");

        return response;
    }

    @PatchMapping("/{guidelineId}")
    public GuidelineResponse deleteGuideline(@PathVariable String guidelineId) {
        return guidelineService.deleteGuideline(guidelineId);
    }
}
