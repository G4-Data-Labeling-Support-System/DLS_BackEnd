package com.group4.DLS.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.group4.DLS.domain.entity.Project;
import com.group4.DLS.domain.enums.ProjectStatus;

public interface ProjectRepository extends JpaRepository<Project, String> {
    boolean existsByProjectNameAndProjectStatusNot(
        String projectName,
        ProjectStatus status
    );
    
    List<Project> findByProjectStatusIn(
        List<ProjectStatus> statuses
    );
    
    Optional<Project> findByProjectIdAndProjectStatusIn(
        String projectId,
        List<ProjectStatus> statuses
    );
}
