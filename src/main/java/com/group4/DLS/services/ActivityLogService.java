package com.group4.DLS.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.group4.DLS.domain.entity.ActivityLog;
import com.group4.DLS.domain.entity.User;
import com.group4.DLS.domain.enums.ActionType;
import com.group4.DLS.repositories.ActivityLogRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ActivityLogService {

    ActivityLogRepository activityLogRepository;

    // Get All Logs
    public void log(
            String actionType,
            String entityName,
            String entityId,
            String description,
            String ipAddress,
            User user) {
        ActivityLog log = ActivityLog.builder()
                .actionType(actionType)
                .entityName(entityName)
                .entityId(entityId)
                .description(description)
                .ipAddress(ipAddress)
                .user(user)
                .build();

        activityLogRepository.save(log);
    }
}
