package com.group4.DLS.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.group4.DLS.domain.dto.request.ActivityLogRequest;
import com.group4.DLS.domain.dto.response.ActivityLogResponse;
import com.group4.DLS.domain.entity.ActivityLog;

@Mapper(componentModel = "spring")
public interface ActivityLogMapper {

    ActivityLogResponse toActivityLogResponse(ActivityLog log);

    List<ActivityLog> toActivityLogResponse(List<ActivityLog> log);

    @Mapping(target = "logId", ignore = true)
    @Mapping(target = "timestamp", ignore = true)
    ActivityLog createActivityLogFromRequest(
        ActivityLogRequest request
    );
}
