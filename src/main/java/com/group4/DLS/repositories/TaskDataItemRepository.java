package com.group4.DLS.repositories;

import com.group4.DLS.domain.entity.TaskDataItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskDataItemRepository extends JpaRepository<TaskDataItem, String> {
}
