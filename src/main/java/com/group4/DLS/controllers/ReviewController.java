package com.group4.DLS.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group4.DLS.domain.dto.request.DatasetCreationRequest;
import com.group4.DLS.domain.dto.request.ReviewUpdateRequest;
import com.group4.DLS.domain.dto.response.ApiResponse;
import com.group4.DLS.domain.dto.response.ReviewResponse;
import com.group4.DLS.services.ReviewService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Reviews", description = "Review management endpoints")
@SecurityRequirement(name = "Bearer Authentication")
public class ReviewController {

    ReviewService reviewService;


    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('REVIEWER','MANAGER')")
    public ApiResponse<ReviewResponse> reviewTask(
            @ModelAttribute ReviewUpdateRequest request) throws IOException {

        ApiResponse<ReviewResponse> response = new ApiResponse<>();

        response.setCode(200);
        response.setMessage("Review updated successfully");
        response.setData(reviewService.reviewed(request));

        return response;
    }

    // GET reviews by annotationId
    @GetMapping("/annotation/{annotationId}")
    @PreAuthorize("hasAnyRole('MANAGER','REVIEWER','ANNOTATOR', 'ADMIN')")
    public ApiResponse<List<ReviewResponse>> getReviewsByAnnotationId(
            @PathVariable String annotationId) {

        ApiResponse<List<ReviewResponse>> response = new ApiResponse<>();
        response.setCode(200);
        response.setData(reviewService.reviewsOfAnntation(annotationId));
        response.setMessage("Reviews retrieved successfully");

        return response;
    }

    // GET reviews by taskdataitemId
    @GetMapping("/TaskDataItem/{TaskDataItemId}")
    @PreAuthorize("hasAnyRole('MANAGER','REVIEWER','ANNOTATOR', 'ADMIN')")
    public ApiResponse<List<ReviewResponse>> getReviewsByTaskDataItemId(
            @PathVariable String TaskDataItemId) {

        ApiResponse<List<ReviewResponse>> response = new ApiResponse<>();
        response.setCode(200);
        response.setData(reviewService.getReviewByTaskDatatItem(TaskDataItemId));
        response.setMessage("Reviews retrieved successfully");

        return response;
    }
}