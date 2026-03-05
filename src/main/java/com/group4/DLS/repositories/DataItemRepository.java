package com.group4.DLS.repositories;

import com.group4.DLS.domain.entity.Dataitem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataItemRepository extends JpaRepository<Dataitem, String> {
    //find all data items by dataset id
    List<Dataitem> findByDataset_DatasetId(String datasetId);
}
