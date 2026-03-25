package com.group4.DLS.repositories;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.group4.DLS.domain.entity.Assignment;

import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, String> {

    boolean existsByAssignmentName(String assignmentName);

    boolean existsByDataset_DatasetIdAndAssignmentIdNot(String datasetId, String assignmentId);

    List<Assignment> findByProject_ProjectId(String projectId);

    List<Assignment> findByAssignedTo_UserId(String userId);

    List<Assignment> findByReviewedBy_UserId(String reviewerId);

    // Find assignment by dataset_id
    Assignment findByDatasetDatasetId(String datasetId);
}
