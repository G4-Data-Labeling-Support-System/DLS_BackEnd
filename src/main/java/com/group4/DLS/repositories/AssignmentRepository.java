package com.group4.DLS.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.group4.DLS.domain.entity.Assignment;
import com.group4.DLS.domain.enums.AssignmentStatus;

import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, String> {

    boolean existsByAssignmentName(String assignmentName);

    List<Assignment> findByProject_ProjectId(String projectId);

    List<Assignment> findByAssignedTo_UserId(String userId);

    // Find assignment by dataset_id
    Assignment findByDatasetDatasetId(String datasetId);
}
