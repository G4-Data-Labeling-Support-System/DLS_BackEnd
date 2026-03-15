package com.group4.DLS.repositories;


import com.group4.DLS.domain.entity.Label;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LabelRepository extends JpaRepository<Label, String> {

    List<Label> findByDataset_DatasetId(String datasetId);

    boolean existsByLabelNameAndDataset_DatasetId(String labelName, String datasetId);
}