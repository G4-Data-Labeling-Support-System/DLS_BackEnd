package com.group4.DLS.repositories;

import com.group4.DLS.domain.entity.TaskDataItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskDataItemRepository extends JpaRepository<TaskDataItem, String> {
        List<TaskDataItem> findByTask_TaskId(String taskId);


    void deleteByDataitem_Dataset_DatasetId(String datasetId);

    @Modifying
    @Query("""
UPDATE TaskDataItem t
SET t.itemIndex = t.itemIndex - 1
WHERE t.task.taskId = :taskId
AND t.itemIndex > :index
""")
    void decreaseIndexAfter(@Param("taskId") String taskId,
                            @Param("index") int index);

    @Modifying
    @Query("""
DELETE FROM TaskDataItem t
WHERE t.dataitem.itemId = :dataitemId
""")
    void deleteByDataitemId(@Param("dataitemId") String dataitemId);
}
