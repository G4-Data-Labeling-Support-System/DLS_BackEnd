package com.group4.DLS.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.group4.DLS.domain.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, String> {

    boolean existsByProjectName(String projectName);
   // List<Project> findAllByCreatedBy(User createdBy);
   //    List<Project> findAllByCreatedBy(User createdBy);
}
