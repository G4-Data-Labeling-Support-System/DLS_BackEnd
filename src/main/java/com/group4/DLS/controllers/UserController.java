package com.group4.DLS.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.group4.DLS.domain.dto.request.UserCreationRequest;
import com.group4.DLS.domain.dto.request.UserPasswordChangeRequest;
import com.group4.DLS.domain.dto.request.UserUpdateRequest;
import com.group4.DLS.domain.dto.response.ApiResponse;
import com.group4.DLS.domain.dto.response.UserResponse;
import com.group4.DLS.domain.entity.User;
import com.group4.DLS.services.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/v1/users")
public class UserController {
    UserService userService;

    /*
    * ================
    * CRUD Operations
    * ===============
    */
    @GetMapping
    public ApiResponse<List<User>> getAllUsers() {
        ApiResponse<List<User>> response = new ApiResponse<>();
        
        response.setCode(200);
        response.setData(userService.getAllUsers());
        response.setMessage("Users retrieved successfully");

        return response;
    }

    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getUserById(@PathVariable String id) {
        ApiResponse<UserResponse> response = new ApiResponse<>();
        
        response.setCode(200);
        response.setData(userService.getUserById(id));
        response.setMessage("User retrieved successfully");

        return response;
    }

    @PostMapping
    public ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        ApiResponse<UserResponse> response = new ApiResponse<>();

        response.setCode(201);
        response.setData(userService.createUser(request));
        response.setMessage("User created successfully");

        return response;
    }

    @PutMapping("/update/{id}")
    public ApiResponse<UserResponse> updateUser(@PathVariable String id, @RequestBody @Valid UserUpdateRequest request) {
        ApiResponse<UserResponse> response = new ApiResponse<>();

        response.setCode(200);
        response.setData(userService.updateUser(id, request));
        response.setMessage("User updated successfully");

        return response;
    }

    @PutMapping("/update/password/{id}")
    public ApiResponse<UserResponse> updateUserPassword(@PathVariable String id, @RequestBody @Valid UserPasswordChangeRequest request) {
        ApiResponse<UserResponse> response = new ApiResponse<>();

        response.setCode(200);
        response.setData(userService.updateUserPassword(id, request));
        response.setMessage("Password change successfully");

        return response;
    }

    @PatchMapping("/{id}/deactivate")
    public ApiResponse<UserResponse> deleteUser(@PathVariable String id) {
        ApiResponse<UserResponse> response = new ApiResponse<>();

        response.setCode(200);
        response.setMessage("Deactivate user successfully");
        response.setData(userService.deactivateUser(id));

        return response;
    }

    @PatchMapping("/{id}/activate")
    public ApiResponse<UserResponse> activateUser(@PathVariable String id) {
        ApiResponse<UserResponse> response = new ApiResponse<>();

        response.setCode(200);
        response.setMessage("Activate user successfully");
        response.setData(userService.avtivateUser(id));

        return response;
    }
}
