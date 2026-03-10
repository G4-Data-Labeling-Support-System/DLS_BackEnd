package com.group4.DLS.services;


import com.group4.DLS.domain.dto.response.TaskResponse;
import com.group4.DLS.domain.entity.Assignment;
import com.group4.DLS.exceptions.AppException;
import com.group4.DLS.exceptions.enums.ErrorCode;
import com.group4.DLS.mappers.TaskMapper;
import com.group4.DLS.repositories.AssignmentRepository;
import com.group4.DLS.repositories.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class TaskService {

    TaskRepository taskRepository;
    TaskMapper taskMapper;
    AssignmentRepository assignmentRepository;

     // ================= GET ALL TASKS =================
     public List<TaskResponse> getAllTasks() {
        List<TaskResponse> tasks = taskRepository.findAll()
                .stream()
                .map(taskMapper::toResponse)
                .toList();
        if(tasks.isEmpty()){
            throw new AppException(ErrorCode.TASK_NOT_FOUND);
        }
        return tasks;
    }

    //crete task for assignment
     public void createTaskForAssignment(String assignmentId) {
         //check assignment exist
         Assignment assignment = assignmentRepository.findById(assignmentId)
                 .orElseThrow(() -> new AppException(ErrorCode.ASSIGNMENT_NOT_FOUND));

        Integer numOfDataItems = assignment.getTotalItems();
        while(numOfDataItems > 0){
            for
        }
}
