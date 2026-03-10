package com.group4.DLS.repositories;

import com.group4.DLS.domain.dto.response.TaskResponse;
import com.group4.DLS.domain.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {

    List<Task> findByAssignment_AssignmentId(String assignmentId);
}
