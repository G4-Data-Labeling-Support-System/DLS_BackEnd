package com.group4.DLS.mappers;

import com.group4.DLS.domain.dto.response.DataItemResponse;
import com.group4.DLS.domain.dto.response.TaskResponse;
import com.group4.DLS.domain.entity.Dataitem;
import com.group4.DLS.domain.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    @Mapping(target = "assignmentId", source = "assignment.assignmentId")
    TaskResponse toResponse(Task task);

    List<TaskResponse> toTaskResponse(List<Task> dataitems);
}
