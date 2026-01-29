package com.group4.DLS.service;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.group4.DLS.domain.dto.request.UserCreationRequest;
import com.group4.DLS.domain.dto.request.UserUpdateRequest;
import com.group4.DLS.domain.dto.response.UserResponse;
import com.group4.DLS.domain.entity.User;
import com.group4.DLS.exceptions.AppException;
import com.group4.DLS.exceptions.enums.ErrorCode;
import com.group4.DLS.mapper.UserMapper;
import com.group4.DLS.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get user by ID
    public UserResponse getUserById(String id) {
        return userMapper.toUserResponse(userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
    }

    // Create user with email uniqueness check
    public UserResponse createUser(UserCreationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.USER_EXISTS);
        }

        User user = userMapper.toUser(request);
        PasswordEncoder encoder = new BCryptPasswordEncoder(10);
        user.setPassword(encoder.encode(request.getPassword()));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    // Update user detailes
    public UserResponse updateUser(String id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (user != null) {
            userMapper.updateUserFromRequest(request, user);
            return userMapper.toUserResponse(userRepository.save(user));
        }

        return null;
    }

    // Delete user by ID
    public void deleteUser(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        
        if (user != null) {
            userRepository.delete(user);
            return;
        }
    }       
}
