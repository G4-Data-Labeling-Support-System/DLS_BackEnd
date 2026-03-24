package com.group4.DLS.repositories;

import com.group4.DLS.domain.entity.Annotation;
import com.group4.DLS.domain.entity.Task;
import com.group4.DLS.domain.enums.AnnotationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnotationRepository extends JpaRepository<Annotation, String> {
    List<Annotation> findByTask_Assignment_AssignmentId(String assignmentId);
    Annotation findByTask_TaskIdAndDataitem_ItemId(String taskId, String ItemId);
    Annotation findAnnotationByDataitem_ItemId(String dataItemId);

    List<Annotation> findAnnotationsByTask(Task task);

    List<Annotation> findAnnotationsByTask_TaskId(String taskId);

    //get annotaions by task and status not have
    List<Annotation> findByTaskAndAnnotationStatusNot(Task task, AnnotationStatus status);
}
