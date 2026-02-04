package com.group4.DLS.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.group4.DLS.domain.entity.Project;
import com.group4.DLS.domain.entity.enums.ProjectStatus;

public interface ProjectRepository extends JpaRepository<Project, String> {
    boolean existsByProjectName(String projectName);
    List<Project> findByStatusIn(List<ProjectStatus> statuses);

   // List<Project> findAllByCreatedBy(User createdBy);
   //    List<Project> findAllByCreatedBy(User createdBy);
}
