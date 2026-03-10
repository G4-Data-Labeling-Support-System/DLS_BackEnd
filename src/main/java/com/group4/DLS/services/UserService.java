package com.group4.DLS.services;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.group4.DLS.domain.dto.request.UserCreationRequest;
import com.group4.DLS.domain.dto.request.UserPasswordChangeRequest;
import com.group4.DLS.domain.dto.request.UserUpdateRequest;
import com.group4.DLS.domain.dto.response.UserResponse;
import com.group4.DLS.domain.entity.User;
import com.group4.DLS.domain.entity.enums.UserStatus;
import com.group4.DLS.exceptions.AppException;
import com.group4.DLS.exceptions.enums.ErrorCode;
import com.group4.DLS.mappers.UserMapper;
import com.group4.DLS.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    ActivityLogService logService;
    SeaweedFilerUploadService seaweedFilerUploadService;

    // Get all users
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();

        return userMapper.toUserResponse(users);
    }

    // Get user by ID
    public UserResponse getUserById(String id) {
        return userMapper.toUserResponse(userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
    }

    // Create user with email uniqueness check
    public UserResponse createUser(UserCreationRequest request) {
        if (userRepository.existsByEmail(request.getEmail()) && request.getUserStatus() == UserStatus.INACTIVE) {
            throw new AppException(ErrorCode.USER_EXISTS);
        }

        User user = userMapper.toUser(request);
        PasswordEncoder encoder = new BCryptPasswordEncoder(10);
        user.setPassword(encoder.encode(request.getPassword()));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    // Update user details
    public UserResponse updateUser(String id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (user != null && user.getUserStatus().equals(UserStatus.ACTIVE)) {
            userMapper.updateUserFromRequest(request, user);

            // Log action
            // logService.log(
            //         "UPDATE_USER_DETAILS",
            //         "USER",
            //         user.getUserId(),
            //         "Updated user details: " + user.getUsername());

            return userMapper.toUserResponse(userRepository.save(user));
        }

        return null;
    }

    // Update user password
    public UserResponse updateUserPassword(String id, UserPasswordChangeRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Check if user active or not
        if (user.getUserStatus() != UserStatus.ACTIVE) {
            throw new AppException(ErrorCode.USER_NOT_ACTIVE);
        }

        // Check old password
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.INVALID_OLD_PASSWORD);
        }

        // New password must not equals the old one
        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.NEW_PASSWORD_SAME_AS_OLD);
        }

        // New password must match the confirm one
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new AppException(ErrorCode.PASSWORD_CONFIRM_NOT_MATCH);
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        // Log action
        // logService.log(
        //         "UPDATE_USER_PASSWORD",
        //         "USER",
        //         user.getUserId(),
        //         "Updated user details: " + user.getUsername());

        return userMapper.toUserResponse(userRepository.save(user));

    }

    // Deactivate user by ID
    public UserResponse deactivateUser(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (user.getUserStatus() == UserStatus.INACTIVE) {
            throw new AppException(ErrorCode.USER_ALREADY_ACTIVE);
        }
        
        user.setUserStatus(UserStatus.INACTIVE);

        // Log action
        // logService.log(
        //         "DEACTIVATE USER",
        //         "USER",
        //         user.getUserId(),
        //         "Deactivated a user: " + user.getUsername());

        return userMapper.toUserResponse(userRepository.save(user));
    }    
    
    // Avtivate user by ID
    public UserResponse avtivateUser(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        
        if (user.getUserStatus() == UserStatus.ACTIVE) {
            throw new AppException(ErrorCode.USER_ALREADY_ACTIVE);
        }

        user.setUserStatus(UserStatus.ACTIVE);

        // Log action
        // logService.log(
        //         "ACTIVATE USER",
        //         "USER",
        //         user.getUserId(),
        //         "Activated a user: " + user.getUsername());

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Transactional
    public UserResponse uploadAvatar(String userId, MultipartFile file) throws Exception {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (file.getSize() > 10 * 1024 * 1024) {
            throw new AppException(ErrorCode.OVER_SIZE_FILE);
        }

        //  Upload lên SeaweedFS
        String imageUrl = seaweedFilerUploadService.uploadImage(file, "avatars");

        // Lưu URL vào DB
        user.setCoverImage(imageUrl);

        return userMapper.toUserResponse(userRepository.save(user));

    }

    @Transactional
    public UserResponse editAvatar(String userId, MultipartFile file) throws Exception {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (file.getSize() > 10 * 1024 * 1024) {
            throw new AppException(ErrorCode.OVER_SIZE_FILE);
        }

//        // Lưu ảnh cũ
//        String oldImageUrl = user.getCoverImage();

        // Upload ảnh mới
        String newImageUrl = seaweedFilerUploadService.uploadImage(file, "avatars");

        // Update DB
        user.setCoverImage(newImageUrl);

//        // Xóa ảnh cũ (sau khi DB update thành công)
//        if (oldImageUrl != null && !oldImageUrl.isBlank()) {
//            try {
//                seaweedFilerUploadService.deleteImageByUrl(oldImageUrl);
//            } catch (Exception e) {
//                // log lại, không throw để tránh rollback transaction
//                System.out.println("Cannot delete old avatar: " + e.getMessage());
//            }
//        }

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Transactional
    public UserResponse deleteAvatar(String userId) throws Exception {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setCoverImage(null);

        return userMapper.toUserResponse(userRepository.save(user));

    }
}
