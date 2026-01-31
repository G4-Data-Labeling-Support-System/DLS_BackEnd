package com.group4.DLS.repository;


import com.group4.DLS.domain.entity.Dataset;
import com.group4.DLS.domain.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DatasetRepository extends JpaRepository<Dataset, String> {

    List<Dataset> findByProject(Project project);
}
