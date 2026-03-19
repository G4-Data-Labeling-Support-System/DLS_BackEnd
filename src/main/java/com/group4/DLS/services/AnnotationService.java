package com.group4.DLS.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group4.DLS.domain.dto.request.AnnotationCreationRequest;
import com.group4.DLS.domain.dto.request.AnnotationItemRequest;
import com.group4.DLS.domain.dto.response.AnnotationResponse;
import com.group4.DLS.domain.entity.Annotation;
import com.group4.DLS.domain.entity.Dataitem;
import com.group4.DLS.domain.entity.Label;
import com.group4.DLS.domain.entity.Task;
import com.group4.DLS.domain.entity.User;
import com.group4.DLS.domain.enums.AnnotationStatus;
import com.group4.DLS.exceptions.AppException;
import com.group4.DLS.exceptions.enums.ErrorCode;
import com.group4.DLS.mappers.AnnotationMapper;
import com.group4.DLS.repositories.AnnotationRepository;
import com.group4.DLS.repositories.DataItemRepository;
import com.group4.DLS.repositories.LabelRepository;
import com.group4.DLS.repositories.TaskRepository;
import com.group4.DLS.repositories.UserRepository;

import java.util.ArrayList;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AnnotationService {

    AnnotationRepository annotationRepository;
    TaskRepository taskRepository;
    DataItemRepository dataItemRepository;
    LabelRepository labelRepository;

    ReviewService reviewService;

    AnnotationMapper annotationMapper;

    // function to change to jason to save database
    private String convertToJson(Object value) {
        try {
            return new ObjectMapper().writeValueAsString(value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // ================= GET ALL ANNOTATION FOR CURRENT ASSIGNMENT =================
    public List<AnnotationResponse> getAnnotationsByAssignmentId(String assignmentId) {
        List<Annotation> annotations = annotationRepository.findByTask_Assignment_AssignmentId(assignmentId);

        if (annotations != null) {
            return annotationMapper.toAnnotationResponses(annotations);
        }

        return Collections.emptyList();
    }

    //public all annnotation by task and not status
    public List<Annotation> getAnnotationsByTaskAndNotStatus(Task task,AnnotationStatus status){
        if(task == null){
            throw new AppException(ErrorCode.ANNOTATION_NOT_FOUND);
        }

        List<Annotation> annotations = annotationRepository.findByTaskAndAnnotationStatusNot(task, status);

        if(annotations.isEmpty()){
            throw new AppException(ErrorCode.ANNOTATION_STATUS_HAVE_REJECTED);
        }

        return annotations;
    }

    // ================= CREATE NEW ANNOTATION =================
    @Transactional
    public List<AnnotationResponse> createAnnotation(AnnotationCreationRequest request) {

        // Get current task
        Task task = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new AppException(ErrorCode.TASK_NOT_FOUND));

        // Get logged in user
        var auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        List<Annotation> annotationsToSave = new ArrayList<>();

        for (AnnotationItemRequest item : request.getAnnotations()) {

            // Get dataitem for current annotation
            Dataitem dataitem = dataItemRepository.findById(item.getDataitemId())
                    .orElseThrow(() -> new AppException(ErrorCode.DATAITEM_NOT_FOUND));

            Annotation annotation = new Annotation();

            annotation.setAnnotationData(convertToJson(item.getAnnotationData()));
            annotation.setAnnotationType(item.getAnnotationType());
            annotation.setAnnotationStatus(item.getAnnotationStatus());
            annotation.setAnnotationConfidence(item.getAnnotationConfidence());
            annotation.setComment(item.getComment());

            annotation.setTask(task);
            annotation.setUser(user);
            annotation.setDataitem(dataitem);

            // Handel set labels
            if (item.getLabelIds() != null && !item.getLabelIds().isEmpty()) {
                List<Label> labels = labelRepository.findAllById(item.getLabelIds());

                if (labels.size() != item.getLabelIds().size()) {
                    throw new AppException(ErrorCode.LABEL_NOT_FOUND);
                }

                annotation.setLabels(labels);
            }

            annotationsToSave.add(annotation);
        }

        return annotationMapper.toAnnotationResponses(annotationRepository.saveAll(annotationsToSave));
    }

    // ================= REMOVE ANNOTATION BY ASSINGMENT_ID =================
    public void removeAnnotationByAssignmentId(String assignmentId) {

        // Get all annotation related to task and assignment
        List<Annotation> annotations = annotationRepository.findByTask_Assignment_AssignmentId(assignmentId);

        // if (annotations.isEmpty()) {
        //     return;
        // }

        // Extract annotationIds
        // List<String> annotationIds = annotations.stream()
        //         .map(Annotation::getAnnotationId)
        //         .toList();

        // Delete reviews in batch
        // reviewService.removeReviewByAnnotationId(annotationIds);

        // Delete annotations in batch
        for (Annotation annotation : annotations) {
            annotation.setAnnotationStatus(AnnotationStatus.INACTIVE);
        }
        annotationRepository.saveAll(annotations);
    }
}
