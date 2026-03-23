package com.group4.DLS.controllers;

import com.group4.DLS.domain.dto.request.AnnotationCreationRequest;
import com.group4.DLS.domain.dto.response.AnnotationResponse;
import com.group4.DLS.domain.dto.response.ApiResponse;
import com.group4.DLS.services.AnnotationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/annotations")
@RequiredArgsConstructor
@Tag(name = "Annotations", description = "Annotation management endpoints")
@SecurityRequirement(name = "Bearer Authentication")
public class AnnotationController {

    private final AnnotationService annotationService;

    // ================= CREATE NEW ANNOTATION =================
    @PostMapping("/submit")
    @PreAuthorize("hasAnyRole('ANNOTATOR', 'MANAGER')")
    @Operation(
        summary = "Create new annotation",
        description = "Create new annotation"
    )
    public ApiResponse<List<AnnotationResponse>> createAnnotationApiResponse(
        @RequestBody AnnotationCreationRequest request
    ) {

        ApiResponse<List<AnnotationResponse>> response = new ApiResponse<>();

        response.setCode(200);
        response.setData(annotationService.createAnnotation(request));
        response.setMessage("Annotation submit successfully");

        return response;
    }
}