package com.group4.DLS.controllers;

import com.group4.DLS.domain.dto.request.ReviewCreationRequest;
import com.group4.DLS.domain.dto.response.ApiResponse;
import com.group4.DLS.domain.dto.response.ReviewResponse;
import com.group4.DLS.domain.entity.Review;
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
@RequestMapping("/api/v1/reviews")
@Tag(name = "Reviews", description = "Annotation review endpoints")
@SecurityRequirement(name = "Bearer Authentication")
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "Create reviews for annotations")
    @PostMapping
    @PreAuthorize("hasRole('REVIEWER')")
    public ApiResponse<List<ReviewResponse>> createReview(@RequestBody ReviewCreationRequest request) {
        ApiResponse<List<ReviewResponse>> response = new ApiResponse<>();
        response.setData(reviewService.createReview(request));
        response.setCode(200);
        response.setMessage("Create reviews successfully");
        return response;
    }
}