package com.group4.DLS.controllers;


import com.group4.DLS.domain.dto.request.ReviewCreationRequest;
import com.group4.DLS.domain.dto.response.ApiResponse;
import com.group4.DLS.domain.dto.response.ReviewResponse;
import com.group4.DLS.services.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Reviews", description = "Annotation review endpoints")
@SecurityRequirement(name = "Bearer Authentication")
public class ReviewController {

    private final ReviewService reviewService;

    /*
     * REVIEW ANNOTATION
     */
    @PostMapping("/api/v1/annotations/{annotationId}/reviews")
    @PreAuthorize("hasAnyRole('REVIEWER','MANAGER','ADMIN')")
    public ApiResponse<ReviewResponse> reviewAnnotation(
            @PathVariable String annotationId,
            @RequestBody ReviewCreationRequest request) {

        return ApiResponse.<ReviewResponse>builder()
                .code(200)
                .message("Annotation reviewed successfully")
                .data(reviewService.reviewAnnotation(annotationId, request))
                .build();
    }

}