package com.group4.DLS.services;

import com.group4.DLS.domain.dto.response.TaskResponse;
import com.group4.DLS.domain.entity.Annotation;
import com.group4.DLS.domain.entity.Assignment;
import com.group4.DLS.domain.entity.Dataitem;
import com.group4.DLS.domain.entity.Task;
import com.group4.DLS.domain.enums.AnnotationStatus;
import com.group4.DLS.domain.enums.TaskStatus;
import com.group4.DLS.domain.enums.TaskType;
import com.group4.DLS.exceptions.AppException;
import com.group4.DLS.exceptions.enums.ErrorCode;
import com.group4.DLS.mappers.TaskMapper;
import com.group4.DLS.repositories.AssignmentRepository;
import com.group4.DLS.repositories.DataItemRepository;
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
    TaskDataItemService taskDataItemService;
    DataItemRepository dataItemRepository;
    AnnotationService annotationService;
    ReviewService reviewService;

    // ================= GET ALL TASKS =================
    public List<TaskResponse> getAllTasks() {
        List<TaskResponse> tasks = taskRepository.findAll()
                .stream()
                .map(taskMapper::toResponse)
                .toList();
        if (tasks.isEmpty()) {
            throw new AppException(ErrorCode.TASK_NOT_FOUND);
        }
        return tasks;
    }

    // ================= CREATE NEW TASK FOR CURRENT ASSIGNMENT =================
    public void createTasksForAssignment(String assignmentId) {

        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new AppException(ErrorCode.ASSIGNMENT_NOT_FOUND));

        List<Dataitem> dataitems = dataItemRepository.findByDataset_DatasetId(
                assignment.getDataset().getDatasetId());

        int maxPerTask = 20; // số lượng dataitem tối đa mỗi task có thể xử lý, có thể điều chỉnh tùy theo
                             // yêu cầu
        int taskIndex = 1; // bắt đầu từ 1 để đặt tên TASK-01, TASK-02, ...

        for (int i = 0; i < dataitems.size(); i += maxPerTask) {// duyệt qua danh sách dataitems theo từng batch có kích
                                                                // thước maxPerTask

            int end = Math.min(i + maxPerTask, dataitems.size());// đảm bảo không vượt quá kích thước của danh sách

            List<Dataitem> batch = dataitems.subList(i, end);// lấy một batch con của dataitems để tạo task

            // tạo task
            Task task = new Task();
            task.setAssignment(assignment);
            task.setTaskName(String.format("TASK-%02d", taskIndex++));// format tên task theo TASK-XX
            task.setTaskType(TaskType.BATCH);
            task.setTaskStatus(TaskStatus.NOT_STARTED);
            task.setCompletedCount(0);

            taskRepository.save(task);
            taskDataItemService.createTaskDataItem(task, batch);
        }
    }

    // get annotation have not reject by task
    public List<Annotation> getAnnotationsNotRejected(Task task) {
        return annotationService.getAnnotationsByTaskAndNotStatus(task, AnnotationStatus.REJECTED);
    }

    // ================= GET TASK BY ASSIGNMENT_ID =================
    public List<TaskResponse> getTasksByAssignmentId(String assignmentId) {
        // check assignment exist
        if (!assignmentRepository.existsById(assignmentId)) {
            throw new AppException(ErrorCode.ASSIGNMENT_NOT_FOUND);
        }

        // get tasks by assignmentId
        List<Task> tasks = taskRepository.findByAssignment_AssignmentId(assignmentId);
        if (tasks.isEmpty()) {
            throw new AppException(ErrorCode.TASK_NOT_FOUND);
        }

        for (Task task: tasks){
            //so sánh số item trong task với số annotation đã submitted + approved
            //case1: nếu 20 annotation submitted = với số item task có là task đó đang cần review
            //case2: nếu 10 item approved và 10 item submitted sau khi sửa
            // nếu có annatation có status là rejected thì không set lại
            if(task.getTaskDataitems().size() == getAnnotationsNotRejected(task).size()){
                reviewService.createReviews(task);
                task.setTaskStatus(TaskStatus.IN_PROGRESS);
                task.setFlagForReview(true);
            }
        }

        return taskMapper.toTaskResponse(tasks);
    }

    // ================= REMOVE TASK BY ASSIGNMENT_ID =================
    public void removeTasksByAssignmentId(String assignmentId) {

        // Check assignment exists
        List<Task> tasks = taskRepository.findByAssignment_AssignmentId(assignmentId);

        if (tasks.isEmpty()) {
            throw new AppException(ErrorCode.TASK_NOT_FOUND);
        }

        // Soft delete = set status INACTIVE
        for (Task task : tasks) {
            task.setTaskStatus(TaskStatus.INACTIVE);
        }

        // Save all
        taskRepository.saveAll(tasks);
    }
}
