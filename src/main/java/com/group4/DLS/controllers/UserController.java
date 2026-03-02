package com.group4.DLS.controllers;

import org.springframework.web.bind.annotation.*;

import com.group4.DLS.domain.dto.request.UserCreationRequest;
import com.group4.DLS.domain.dto.request.UserPasswordChangeRequest;
import com.group4.DLS.domain.dto.request.UserUpdateRequest;
import com.group4.DLS.domain.dto.response.ApiResponse;
import com.group4.DLS.domain.dto.response.UserResponse;
import com.group4.DLS.domain.entity.User;
import com.group4.DLS.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/v1/users")
@Tag(name = "Users", description = "User management endpoints")
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {
    UserService userService;

    /*
    * ================
    * Get All User
    * ===============
    */
    @GetMapping
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN', 'ANNOTATOR', 'REVIEWER')")
    @Operation(
        summary = "Get all users",
        description = "Retrieve a list of all registered users in the system"
    )
    public ApiResponse<List<User>> getAllUsers() {
        ApiResponse<List<User>> response = new ApiResponse<>();
        
        response.setCode(200);
        response.setData(userService.getAllUsers());
        response.setMessage("Users retrieved successfully");

        return response;
    }

    /*
    * ================
    * Get User by Id
    * ===============
    */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN', 'ANNOTATOR', 'REVIEWER')")
    @Operation(
        summary = "Get user by ID",
        description = "Retrieve detailed information about a specific user"
    )
    public ApiResponse<UserResponse> getUserById(@PathVariable String id) {
        ApiResponse<UserResponse> response = new ApiResponse<>();
        
        response.setCode(200);
        response.setData(userService.getUserById(id));
        response.setMessage("User retrieved successfully");

        return response;
    }

    /*
    * ================
    * Create new user
    * ===============
    */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(
        summary = "Create new user",
        description = "Register a new user in the system with role assignment"
    )
    public ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        ApiResponse<UserResponse> response = new ApiResponse<>();

        response.setCode(201);
        response.setData(userService.createUser(request));
        response.setMessage("User created successfully");
        
        return response;
    }

    /*
    * ================
    * Update user by Id
    * ===============
    */
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN', 'ANNOTATOR', 'REVIEWER')")
    @Operation(
        summary = "Update user information",
        description = "Update user profile information (username, full name, email, etc.)"
    )
    public ApiResponse<UserResponse> updateUser(@PathVariable String id, @RequestBody @Valid UserUpdateRequest request) {
        ApiResponse<UserResponse> response = new ApiResponse<>();

        response.setCode(200);
        response.setData(userService.updateUser(id, request));
        response.setMessage("User updated successfully");

        return response;
    }

    /*
    * ================
    * Update user password
    * ===============
    */
    @PutMapping("/update/password/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN', 'ANNOTATOR', 'REVIEWER')")
    @Operation(
        summary = "Change user password",
        description = "Update user password with old password verification"
    )
    public ApiResponse<UserResponse> updateUserPassword(@PathVariable String id, @RequestBody @Valid UserPasswordChangeRequest request) {
        ApiResponse<UserResponse> response = new ApiResponse<>();
        response.setCode(200);
        response.setData(userService.updateUserPassword(id, request));
        response.setMessage("Password change successfully");

        return response;
    }

    /*
    * ================
    * Deactivate user
    * ===============
    */
    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(
        summary = "Deactivate user",
        description = "Deactivate a user account (soft delete)"
    )
    public ApiResponse<UserResponse> deleteUser(@PathVariable String id) {
        ApiResponse<UserResponse> response = new ApiResponse<>();

        response.setCode(200);
        response.setMessage("Deactivate user successfully");
        response.setData(userService.deactivateUser(id));

        return response;
    }

    /*
    * ================
    * Activate user
    * ===============
    */
    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(
        summary = "Activate user",
        description = "Activate a user account"
    )
    public ApiResponse<UserResponse> activateUser(@PathVariable String id) {
        ApiResponse<UserResponse> response = new ApiResponse<>();

        response.setCode(200);
        response.setMessage("Activate user successfully");
        response.setData(userService.avtivateUser(id));

        return response;
    }

    @PutMapping("/{id}/avatar")
    public ApiResponse<UserResponse> uploadAvatar(
            @PathVariable String id,
            @RequestParam MultipartFile file) throws Exception {
        ApiResponse<UserResponse> response = new ApiResponse<>();
        response.setCode(200);
        response.setMessage("Avatar user upload successfully");
        response.setData(userService.uploadAvatar(id, file));
        return response ;
    }

    @PutMapping("/{id}/avatar/edit")
    public ApiResponse<UserResponse> editAvatar(
            @PathVariable String id,
            @RequestParam MultipartFile file) throws Exception {
        ApiResponse<UserResponse> response = new ApiResponse<>();
        response.setCode(200);
        response.setMessage("Avatar user edit successfully");
        response.setData(userService.editAvatar(id, file));
        return response ;
    }

    @DeleteMapping("/{id}/avatar/delete")
    public ApiResponse<UserResponse> deleteAvatar(
            @PathVariable String id) throws Exception {
        ApiResponse<UserResponse> response = new ApiResponse<>();
        response.setCode(200);
        response.setMessage("Avatar user delete successfully");
        response.setData(userService.deleteAvatar(id));
        return response ;
    }



}
