package com.group4.DLS.services;


import com.group4.DLS.domain.dto.response.DataItemResponse;
import com.group4.DLS.domain.dto.response.TaskResponse;
import com.group4.DLS.domain.entity.Assignment;
import com.group4.DLS.domain.entity.Dataitem;
import com.group4.DLS.domain.entity.Task;
import com.group4.DLS.domain.entity.enums.TaskStatus;
import com.group4.DLS.exceptions.AppException;
import com.group4.DLS.exceptions.enums.ErrorCode;
import com.group4.DLS.mappers.TaskMapper;
import com.group4.DLS.repositories.AssignmentRepository;
import com.group4.DLS.repositories.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class TaskService {

    TaskRepository taskRepository;
    TaskMapper taskMapper;
    AssignmentRepository assignmentRepository;
    DataitemService dataitemService;

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

         int numOfDataItems = assignment.getTotalItems();
         int numOfTasks = 0; // Calculate the number of tasks needed
         int batchSize = 100; // Define the batch size for task creation
         List<Task> tasks = new ArrayList<>(); // Get the list of data items for the assignment
         while (numOfDataItems > 0) {
             numOfTasks++;
             if(numOfTasks == batchSize){
                 Task task = new Task();
                    task.setAssignment(assignment);
                    task.setTaskName(generateTaskName());
                    task.setTaskStatus(TaskStatus.NOT_STARTED);
                    tasks.add(task);
                    numOfTasks = 0;
             }
             if(numOfDataItems < batchSize) {
                 Task task = new Task();
                    task.setAssignment(assignment);
                    task.setTaskName(generateTaskName());
                    task.setTaskStatus(TaskStatus.NOT_STARTED);
                    tasks.add(task);
                    break;
             }
             numOfDataItems--;
         }
         taskRepository.saveAll(tasks);

     }

     //đặt tên task theo format TASK-XX
    public String generateTaskName() {
        long count = taskRepository.count() + 1;
        return String.format("TASK-%02d", count);
    }

    //Get Task by assignmentId
    public List<TaskResponse> getTasksByAssignmentId(String assignmentId) {
        //check assignment exist
        if (!assignmentRepository.existsById(assignmentId)) {
            throw new AppException(ErrorCode.ASSIGNMENT_NOT_FOUND);
        }

        //get tasks by assignmentId
        List<Task> tasks = taskRepository.findByAssignment_AssignmentId(assignmentId);
        if (tasks.isEmpty()) {
            throw new AppException(ErrorCode.TASK_NOT_FOUND);
        }

        return taskMapper.toTaskResponse(tasks);
    }

    //Assign task and data items to assignment
    public void assignTaskToAssignment(String assignmentId) {
        createTaskForAssignment(assignmentId);
            Assignment assignment = assignmentRepository.findById(assignmentId)
                    .orElseThrow(() -> new AppException(ErrorCode.ASSIGNMENT_NOT_FOUND));
            List<Task> tasks = taskRepository.findByAssignment_AssignmentId(assignmentId);
            List<DataItemResponse> dataitems = dataitemService.getAllDataitemForDataset(assignment.getDataset().getDatasetId());
            
    }
}
