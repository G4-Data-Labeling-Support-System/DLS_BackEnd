package com.group4.DLS.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.group4.DLS.domain.entity.Dataset;
import com.group4.DLS.domain.entity.Project;

import java.util.List;

public interface DatasetRepository extends JpaRepository<Dataset, String> {

    List<Dataset> findByProject(Project project);
}
