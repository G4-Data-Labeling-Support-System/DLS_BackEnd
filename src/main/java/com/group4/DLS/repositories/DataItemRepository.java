package com.group4.DLS.repositories;

import com.group4.DLS.domain.entity.Dataitem;
import com.group4.DLS.domain.enums.DataItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataItemRepository extends JpaRepository<Dataitem, String> {
    //find all data items by dataset id
    List<Dataitem> findByDataset_DatasetId(String datasetId);

    @Query("""
    SELECT d FROM Dataitem d
    WHERE d.dataset.datasetId = :datasetId
    AND d.itemId NOT IN (
        SELECT ti.dataitem.itemId FROM TaskDataItem ti
    )
""")
    List<Dataitem> findUnassignedDataItems(@Param("datasetId") String datasetId);

    List<Dataitem> findByDataset_DatasetIdAndDataItemStatusOrderByUploadedAtAsc(
            String datasetId,
            DataItemStatus dataItemStatus);
}
