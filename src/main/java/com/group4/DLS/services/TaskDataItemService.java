package com.group4.DLS.services;

import com.group4.DLS.domain.dto.response.DataItemResponse;
import com.group4.DLS.domain.entity.Dataitem;
import com.group4.DLS.domain.entity.Task;
import com.group4.DLS.domain.entity.TaskDataItem;
import com.group4.DLS.mappers.DataItemMapper;
import com.group4.DLS.repositories.TaskDataItemRepository;
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
    DataItemMapper dataItemMapper;

    //Assign dataItem to task
    public void createTaskDataItem(Task task, List<Dataitem> dataitems){

        List<TaskDataItem> list = new ArrayList<>();

        int order = 0;

        for(Dataitem item : dataitems){

            TaskDataItem tdi = new TaskDataItem();
            tdi.setTask(task);
            tdi.setDataitem(item);
            tdi.setAssignedAt(LocalDateTime.now());
            tdi.setItemIndex(order++);
            list.add(tdi);
        }

        taskDataItemRepository.saveAll(list);
    }

        //get dataitems by task id
    public List<DataItemResponse> getDataitemsByTaskId(String taskId) {
        List<TaskDataItem> taskDataItems = taskDataItemRepository.findByTask_TaskId(taskId);
        List<Dataitem> dataitems = new ArrayList<>();
        for (TaskDataItem tdi : taskDataItems) {
            dataitems.add(tdi.getDataitem());
        }
        return dataItemMapper.toDataItemResponse(dataitems);
    }
}
