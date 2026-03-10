package com.group4.DLS.services;

import com.group4.DLS.domain.entity.Dataitem;
import com.group4.DLS.domain.entity.Task;
import com.group4.DLS.domain.entity.TaskDataItem;
import com.group4.DLS.exceptions.AppException;
import com.group4.DLS.exceptions.enums.ErrorCode;
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
    TaskDataItemRepository taskDataItemRepository;

    //Assign dataItem to task
    public void createTaskDataItem(Task task, List<Dataitem> dataitems){

        List<TaskDataItem> list = new ArrayList<>();

        for(Dataitem item : dataitems){

            TaskDataItem tdi = new TaskDataItem();
            tdi.setTask(task);
            tdi.setDataitem(item);
            tdi.setAssignedAt(LocalDateTime.now());

            list.add(tdi);
        }

        taskDataItemRepository.saveAll(list);
    }
}
