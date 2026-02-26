package com.group4.DLS.repositories;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.group4.DLS.domain.entity.Dataset;

@Repository
public interface DatasetRepository extends JpaRepository<Dataset, String> {
    List<Dataset> findByProject_ProjectId(String projectId);
}
