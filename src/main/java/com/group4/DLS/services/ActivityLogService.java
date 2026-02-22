package com.group4.DLS.services;

import org.springframework.stereotype.Service;

import com.group4.DLS.domain.dto.request.ActivityLogRequest;
import com.group4.DLS.domain.dto.response.ActivityLogResponse;
import com.group4.DLS.domain.entity.ActivityLog;
import com.group4.DLS.mappers.ActivityLogMapper;
import com.group4.DLS.repositories.ActivityLogRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ActivityLogService {
    
    ActivityLogRepository activityLogRepository;
    ActivityLogMapper activityLogMapper;

    // Create new Log
    public ActivityLogResponse log(ActivityLogRequest request) {
        ActivityLog log = activityLogMapper.createActivityLogFromRequest(request);
        return activityLogMapper.toActivityLogResponse(activityLogRepository.save(log));
    } 
}
