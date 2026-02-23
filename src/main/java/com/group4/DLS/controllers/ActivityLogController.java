package com.group4.DLS.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api/v1/logs")
@RequiredArgsConstructor
@Tag(name = "Activity Logs", description = "Logs management endpoints")
@SecurityRequirement(name = "Bearer Authentication")
public class ActivityLogController {
    
}
