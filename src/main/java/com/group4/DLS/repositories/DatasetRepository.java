package com.group4.DLS.repositories;

import java.util.List;

import com.group4.DLS.domain.enums.DatasetStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.group4.DLS.domain.entity.Dataset;

@Repository
public interface DatasetRepository extends JpaRepository<Dataset, String> {

    // Find by name
    Dataset findByDatasetName(
        String datasetName
    );

    // Check exists by projectId + name + !status.delete
    boolean existsByProjectProjectIdAndDatasetNameAndDatasetStatusNot(
            String projectId,
            String datasetName,
            DatasetStatus datasetStatus
    );

    // Find all dataset in 1 project
    List<Dataset> findByProject_ProjectId(
        String projectId
    );

}
