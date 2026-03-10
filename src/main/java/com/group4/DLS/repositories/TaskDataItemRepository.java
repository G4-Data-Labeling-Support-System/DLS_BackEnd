package com.group4.DLS.repositories;

import com.group4.DLS.domain.entity.TaskDataItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskDataItemRepository extends JpaRepository<TaskDataItem, String> {
}
