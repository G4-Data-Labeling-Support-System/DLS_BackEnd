package com.group4.DLS.services;

import com.group4.DLS.domain.entity.Dataitem;
import com.group4.DLS.domain.entity.Task;
import com.group4.DLS.domain.entity.TaskDataItem;
import com.group4.DLS.exceptions.AppException;
import com.group4.DLS.exceptions.enums.ErrorCode;
import com.group4.DLS.repositories.DataItemRepository;
import com.group4.DLS.repositories.TaskDataItemRepository;
import com.group4.DLS.repositories.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class TaskDataItemService {
    TaskRepository taskRepository;
    TaskDataItemRepository taskDataItemRepository;
    DataItemRepository dataItemRepository;

    //Assign dataItem to task
    public void createTaskDataItem(String taskId, String dataitemId){
        Task task = taskRepository.findById(taskId).orElseThrow(()-> new AppException(ErrorCode.TASK_NOT_FOUND));
        Dataitem dataitem = dataItemRepository.findById(dataitemId).orElseThrow(()-> new AppException(ErrorCode.DATAITEM_NOT_FOUND));
        TaskDataItem taskDataItem = new TaskDataItem();
        taskDataItem.setTask(task);
        taskDataItem.setDataitem(dataitem);
        taskDataItem.setAssignedAt(LocalDateTime.now());
        taskDataItemRepository.save(taskDataItem);
    }
}
