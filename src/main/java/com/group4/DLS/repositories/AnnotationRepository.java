package com.group4.DLS.repositories;

import com.group4.DLS.domain.entity.Annotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnotationRepository extends JpaRepository<Annotation, String> {
    List<Annotation> findByTask_Assignment_AssignmentId(String assignmentId);
    Annotation findByTask_TaskIdAndDataitem_ItemId(String taskId, String ItemId);
}
