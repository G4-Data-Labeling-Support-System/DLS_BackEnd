package com.group4.DLS.repositories;

import com.group4.DLS.domain.entity.Task;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {

    List<Task> findByAssignment_AssignmentId(String assignmentId);
    List<Task> findByAssignmentAssignmentIdOrderByCreatedAtAsc(String assignmentId);

    @Transactional
    void deleteByAssignment_AssignmentId(String assignmentId);
}
