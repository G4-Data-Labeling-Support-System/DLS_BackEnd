package com.group4.DLS.repository;

import com.group4.DLS.domain.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssignmentRepository extends JpaRepository<Assignment, String> {
    boolean existsByAssignmentName(String assignmentName);
}
