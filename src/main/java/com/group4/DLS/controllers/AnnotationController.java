package com.group4.DLS.controllers;

import com.group4.DLS.domain.dto.request.AnnotationSaveRequest;
import com.group4.DLS.domain.dto.response.ApiResponse;
import com.group4.DLS.domain.entity.Annotation;
import com.group4.DLS.services.AnnotationService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/annotations")
@RequiredArgsConstructor
@Tag(name = "Annotations", description = "Annotation management endpoints")
@SecurityRequirement(name = "Bearer Authentication")
public class AnnotationController {

    private final AnnotationService annotationService;

    @PostMapping("/save")
    @PreAuthorize("hasAnyRole('ANNOTATOR')")
    public ApiResponse<Annotation> saveAnnotation(@RequestBody AnnotationSaveRequest request) {

        ApiResponse<Annotation> response = new ApiResponse<>();

        response.setCode(200);
        response.setData(annotationService.saveAnnotation(request));
        response.setMessage("Annotation saved successfully");

        return response;
    }
}