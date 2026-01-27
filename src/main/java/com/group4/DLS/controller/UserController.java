package com.group4.DLS.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.group4.DLS.domain.dto.request.UserCreationRequest;
import com.group4.DLS.domain.dto.request.UserUpdateRequest;
import com.group4.DLS.domain.dto.response.ApiResponse;
import com.group4.DLS.domain.dto.response.UserResponse;
import com.group4.DLS.domain.entity.User;
import com.group4.DLS.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable String id) {
        ApiResponse<Void> response = new ApiResponse<>();
        userService.deleteUser(id);

        response.setCode(200);
        response.setMessage("User deleted successfully");
        response.setData(null);

        return response;
    }
}
