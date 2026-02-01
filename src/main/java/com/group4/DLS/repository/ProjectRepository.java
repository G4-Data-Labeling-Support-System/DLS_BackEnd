package com.group4.DLS.repository;

import com.group4.DLS.domain.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, String> {

    boolean existsByProjectName(String projectName);
   // List<Project> findAllByCreatedBy(User createdBy);
   //    List<Project> findAllByCreatedBy(User createdBy);
}
