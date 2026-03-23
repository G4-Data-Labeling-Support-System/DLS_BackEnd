package com.group4.DLS.mappers;

import com.group4.DLS.domain.dto.response.TaskDataITemResponse;
import com.group4.DLS.domain.dto.response.TaskResponse;
import com.group4.DLS.domain.entity.Task;
import com.group4.DLS.domain.entity.TaskDataItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskDataitemMapper {

    @Mapping(target = "taskId", source = "task.taskId")
    @Mapping(target = "dataItem", source = "dataitem")
    TaskDataITemResponse toResponse(TaskDataItem taskDataItem);

    List<TaskDataITemResponse> toResponse(List<TaskDataItem> taskDataItems);
}
