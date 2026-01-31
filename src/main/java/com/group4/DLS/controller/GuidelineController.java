package com.group4.DLS.controller;

import com.group4.DLS.domain.dto.request.GuidelineCreateRequest;
import com.group4.DLS.domain.dto.response.ApiResponse;
import com.group4.DLS.domain.dto.response.GuidelineResponse;
import com.group4.DLS.domain.entity.Guideline;
import com.group4.DLS.domain.entity.User;
import com.group4.DLS.service.GuidelineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/guidelines")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class GuidelineController {

    GuidelineService guidelineService;

    /*
     * ======================
     * CREATE (version = 1)
     * ======================
     */
    @PostMapping("/project/{projectId}")
    public ApiResponse<GuidelineResponse> createGuideline(
            @PathVariable String projectId,
            @RequestBody @Valid GuidelineCreateRequest request
    ) {
        ApiResponse<GuidelineResponse> response = new ApiResponse<>();
        response.setCode(201);
        response.setMessage("Guideline created successfully");
        response.setData(guidelineService.create(projectId, request));
        return response;
    }

    /*
     * ======================
     * UPDATE (versioning)
     * ======================
     */
    @PutMapping("/project/{projectId}")
    public ApiResponse<GuidelineResponse> updateGuideline(
            @PathVariable String projectId,
            @RequestBody @Valid GuidelineCreateRequest request
    ) {
        ApiResponse<GuidelineResponse> response = new ApiResponse<>();
        response.setCode(200);
        response.setMessage("Guideline updated successfully");
        response.setData(guidelineService.create(projectId, request));
        return response;
    }

    /*
     * ======================
     * GET latest guideline
     * ======================
     */
    @GetMapping("/project/{projectId}/latest")
    public ApiResponse<GuidelineResponse> getLatestGuideline(
            @PathVariable String projectId
    ) {
        ApiResponse<GuidelineResponse> response = new ApiResponse<>();
        response.setCode(200);
        response.setMessage("Latest guideline retrieved successfully");
        response.setData(guidelineService.getLatest(projectId));
        return response;
    }

    /*
     * ======================
     * GET all guidelines
     * ======================
     */
    @GetMapping("/project/{projectId}")
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
    public ApiResponse<List<Guideline>> getAllGuideline() {
        ApiResponse<List<Guideline>> response = new ApiResponse<>();

        response.setCode(200);
        response.setData(guidelineService.getAllGuideline());
        response.setMessage("GuideLines retrieved successfully");

        return response;
    }
}
