package com.group4.DLS.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.group4.DLS.domain.entity.Assignment;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, String> {
    boolean existsByAssignmentName(String assignmentName);
}
