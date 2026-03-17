package com.group4.DLS.services;

import com.group4.DLS.domain.dto.request.AnnotationSaveRequest;
import com.group4.DLS.domain.dto.response.AnnotationResponse;
import com.group4.DLS.domain.entity.Annotation;
import com.group4.DLS.domain.entity.Dataitem;
import com.group4.DLS.domain.entity.Task;
import com.group4.DLS.domain.enums.AnnotationStatus;
import com.group4.DLS.exceptions.AppException;
import com.group4.DLS.exceptions.enums.ErrorCode;
import com.group4.DLS.mappers.AnnotationMapper;
import com.group4.DLS.repositories.AnnotationRepository;
import com.group4.DLS.repositories.DataItemRepository;
import com.group4.DLS.repositories.LabelRepository;
import com.group4.DLS.repositories.TaskRepository;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AnnotationService {

    AnnotationRepository annotationRepository;
    AnnotationMapper annotationMapper;
    TaskRepository taskRepository;
    DataItemRepository dataItemRepository;
    LabelRepository labelRepository;
    ReviewService reviewService;

    // ================= GET ALL ANNOTATION FOR CURRENT ASSIGNMENT =================
    public List<AnnotationResponse> getAnnotationsByAssignmentId(String assignmentId) {
        List<Annotation> annotations = annotationRepository.findByTask_Assignment_AssignmentId(assignmentId);

        if (annotations != null) {
            return annotationMapper.toAnnotationResponses(annotations);
        }

        return Collections.emptyList();
    }

    // ================= CREATE NEW ANNOTATION =================
    @Transactional
    public Annotation saveAnnotation(AnnotationSaveRequest request) {

        Annotation annotation = annotationMapper.toCreateAnnotation(request);

        Task task = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new RuntimeException("Task not found"));

        Dataitem dataitem = dataItemRepository.findById(request.getDataitemId())
                .orElseThrow(() -> new RuntimeException("Dataitem not found"));

        annotation.setTask(task);
        annotation.setDataitem(dataitem);
        annotation.setUser(task.getAssignment().getAssignedTo());
        annotation.setLabels(request.getLabels());
        annotation.setAnnotationStatus(AnnotationStatus.SUBMITTED);

        return annotationRepository.save(annotation);
    }

    // ================= REMOVE ANNOTATION BY ASSINGMENT_ID =================
    public void removeAnnotationByAssignmentId(String assignmentId) {

        // Get all annotation related to task and assignment
        List<Annotation> annotations = annotationRepository.findByTask_Assignment_AssignmentId(assignmentId);

        if (annotations.isEmpty()) {
            throw new AppException(ErrorCode.ANNOTATION_NOT_FOUND);
        }

        // Extract annotationIds
        List<String> annotationIds = annotations.stream()
                .map(Annotation::getAnnotationId)
                .toList();

        // Delete reviews in batch
        reviewService.removeReviewByAnnotationId(annotationIds);

        // Delete annotations in batch
        annotationRepository.deleteAll(annotations);
    }
}
