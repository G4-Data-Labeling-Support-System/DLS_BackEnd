package com.group4.DLS.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.group4.DLS.domain.entity.Project;
import com.group4.DLS.domain.enums.ProjectStatus;

public interface ProjectRepository extends JpaRepository<Project, String> {
    boolean existsByProjectNameAndStatusNot(
        String projectName,
        ProjectStatus status
    );
    
    List<Project> findByStatusIn(
        List<ProjectStatus> statuses
    );
    
    Optional<Project> findByProjectIdAndStatusIn(
        String projectId,
        List<ProjectStatus> statuses
    );
}
