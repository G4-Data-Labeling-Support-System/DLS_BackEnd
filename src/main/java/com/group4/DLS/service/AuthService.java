package com.group4.DLS.service;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.group4.DLS.config.SecurityConfig;
import com.group4.DLS.domain.dto.request.AuthRequest;
import com.group4.DLS.exceptions.AppException;
import com.group4.DLS.exceptions.enums.ErrorCode;
import com.group4.DLS.repository.UserRepository;

public class AuthService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    public boolean authenticate(AuthRequest request) {
        var user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return passwordEncoder.matches(request.getPassword(), user.getPassword());
    }
}
