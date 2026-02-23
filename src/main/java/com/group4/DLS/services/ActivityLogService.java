package com.group4.DLS.services;

import org.springframework.stereotype.Service;

import com.group4.DLS.domain.entity.ActivityLog;
import com.group4.DLS.domain.entity.User;
import com.group4.DLS.exceptions.AppException;
import com.group4.DLS.exceptions.enums.ErrorCode;
import com.group4.DLS.repositories.ActivityLogRepository;
import com.group4.DLS.repositories.UserRepository;
import com.group4.DLS.security.CurrentUserProvider;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ActivityLogService {
    
    ActivityLogRepository activityLogRepository;
    HttpServletRequest httpServletRequest;
    CurrentUserProvider currentUserProvider;

    public void log(String action,
                String entityName,
                String entityId,
                String description) {
                    
        User user = currentUserProvider.getCurrentUser();
        String ip = httpServletRequest.getRemoteAddr();

        ActivityLog log = ActivityLog.builder()
                .user(user)
                .action(action)
                .entityName(entityName)
                .entityId(entityId)
                .description(description)
                .ipAddress(ip)
                .build();

        if (log != null) {
            activityLogRepository.save(log);
        }
    }
}
