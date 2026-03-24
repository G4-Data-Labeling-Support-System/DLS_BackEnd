package com.group4.DLS.controllers;

import com.group4.DLS.domain.dto.request.AnnotationItemRequest;
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
    @PutMapping("/submit")
    @PreAuthorize("hasAnyRole('ANNOTATOR', 'MANAGER')")
    @Operation(
        summary = "Update annotation",
        description = "Update annotation after Annotator edit"
    )
    public ApiResponse<AnnotationResponse> createAnnotationApiResponse(
        @RequestBody AnnotationItemRequest request
    ) {

        ApiResponse<AnnotationResponse> response = new ApiResponse<>();
        ApiResponse<AnnotationResponse> response = new ApiResponse<>();

        response.setCode(200);
        response.setData(annotationService.updateAnnotation(request));
        response.setMessage("Annotation submit successfully");

        return response;
    }

    @GetMapping("/dataitem/{dataItemId}")
    @PreAuthorize("hasAnyRole('Manager', 'ANNOTATOR', 'REVIEWER')")
    @Operation(
            summary = "Get annotation by DataItem",
            description = "Get annotation by DataItem"
    )
    public ApiResponse<AnnotationResponse> getAnnotationByDataitem
            (@PathVariable String dataItemId){
        ApiResponse<AnnotationResponse> response = new ApiResponse<>();

        response.setCode(200);
        response.setData(annotationService.getAnnotationByDataItemId(dataItemId));
        response.setMessage("Get Annotation successfully");

        return response;
    }

}