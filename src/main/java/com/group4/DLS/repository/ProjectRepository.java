package com.group4.DLS.repository;

import com.group4.DLS.domain.entity.Project;
import com.group4.DLS.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, String> {

    boolean existsByProjectName(String projectName);


    List<Project> findAllByCreatedBy(User createdBy);
}
