package com.group4.DLS.services;

import com.group4.DLS.domain.dto.request.AnnotationSaveRequest;
import com.group4.DLS.domain.entity.Annotation;
import com.group4.DLS.domain.entity.Dataitem;
import com.group4.DLS.domain.entity.Label;
import com.group4.DLS.domain.entity.Task;
import com.group4.DLS.domain.enums.AnnotationStatus;
import com.group4.DLS.mappers.AnnotationMapper;
import com.group4.DLS.repositories.AnnotationRepository;
import com.group4.DLS.repositories.DataItemRepository;
import com.group4.DLS.repositories.LabelRepository;
import com.group4.DLS.repositories.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AnnotationService {

    AnnotationRepository annotationRepository;
    AnnotationMapper annotationMapper;
    TaskRepository taskRepository;
    DataItemRepository  dataItemRepository;
    LabelRepository labelRepository;

    @Transactional
    public Annotation saveAnnotation(AnnotationSaveRequest request) {

        Annotation annotation = annotationRepository
                .findByTask_TaskIdAndDataitem_ItemId(
                        request.getTaskId(),
                        request.getDataitemId());

        if (annotation == null) {

            annotation = annotationMapper.toEntity(request);

            Task task = taskRepository.findById(request.getTaskId())
                    .orElseThrow(() -> new RuntimeException("Task not found"));

            Dataitem dataitem = dataItemRepository.findById(request.getDataitemId())
                    .orElseThrow(() -> new RuntimeException("Dataitem not found"));

            List<Label> labels = labelRepository.findAllById(request.getLabelIds());


            annotation.setTask(task);
            annotation.setDataitem(dataitem);
            annotation.setUser(task.getAssignment().getAssignedTo());
            annotation.setLabels(labels);
            annotation.setAnnotationData(annotationMapper.map(request.getAnnotationData()));
            annotation.setAnnotationStatus(AnnotationStatus.SUBMITTED);

        } else {
            annotation.setAnnotationType(request.getAnnotationType());
            annotation.setAnnotationConfidence(request.getAnnotationConfidence());
            annotation.setComment(request.getComment());
        }

        return annotationRepository.save(annotation);
    }

    void deleteAnnotationByAssignment(String assignmentId){
        List<Annotation> annotations = annotationRepository.findByTask_Assignment_AssignmentId(assignmentId);

        for(Annotation annotation : annotations){
            annotation.setAnnotationStatus(AnnotationStatus.DELETED);
        }
        annotationRepository.saveAll(annotations);
    }
}
