package com.group4.DLS.repositories;

import com.group4.DLS.domain.entity.TaskDataItem;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskDataItemRepository extends JpaRepository<TaskDataItem, String> {
        
        // Find all TaskDataItem that belongs to a specific Task
        List<TaskDataItem> findByTask_TaskId(String taskId);

        // đếm item trong task
        int countByTaskTaskId(String taskId);

        List<TaskDataItem> findByTask_TaskIdOrderByItemIndexAsc(String taskTaskId);


        @Transactional
        List<TaskDataItem> findByTask_Assignment_AssignmentId(String assignmentId);

    TaskDataItem findTaskDataitemByDataitem_ItemId(String dataitemItemId);

    // Delete TaskDataItem for current Assignment
        @Transactional
        @Modifying
        void deleteByTask_Assignment_AssignmentId(String assignmentId);
}
