package com.group4.DLS.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group4.DLS.aop.LogActivity;
import com.group4.DLS.domain.dto.request.AnnotationItemRequest;
import com.group4.DLS.domain.dto.response.AnnotationResponse;
import com.group4.DLS.domain.entity.*;
import com.group4.DLS.domain.enums.AnnotationStatus;
import com.group4.DLS.domain.enums.TaskDataItemStatus;
import com.group4.DLS.exceptions.AppException;
import com.group4.DLS.exceptions.enums.ErrorCode;
import com.group4.DLS.mappers.AnnotationMapper;
import com.group4.DLS.repositories.*;

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

    AnnotationMapper annotationMapper;
    ReviewService reviewService;
    TaskDataItemRepository taskDataItemRepository;

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

    // ================= CREATE NEW ANNOTATION =================
    @Transactional
    @LogActivity(
        action = "Update",
        entity = "Annotation",
        description = "Update annotation",
            entityIdParam = "annotationId"
    )
    public AnnotationResponse updateAnnotation(AnnotationItemRequest request) {

        // Get current task
        Task task = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new AppException(ErrorCode.TASK_NOT_FOUND));

        // Get logged-in user
        var auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        // Get dataitem for current annotation
        Dataitem dataitem = dataItemRepository.findById(request.getDataitemId())
                .orElseThrow(() -> new AppException(ErrorCode.DATAITEM_NOT_FOUND));

        Annotation annotation = annotationRepository.findByTask_TaskIdAndDataitem_ItemId(task.getTaskId(), dataitem.getItemId());

        annotation.setAnnotationData(convertToJson(request.getAnnotationData()));
        annotation.setAnnotationType(request.getAnnotationType());
        annotation.setAnnotationStatus(AnnotationStatus.SUBMITTED);
        annotation.setAnnotationConfidence(request.getAnnotationConfidence());
        annotation.setComment(request.getComment());
        annotation.setTask(task);
        annotation.setUser(user);

        // Handel set labels
        if (request.getLabelIds() != null && !request.getLabelIds().isEmpty()) {
            List<Label> labels = labelRepository.findAllById(request.getLabelIds());

            if (labels.size() != request.getLabelIds().size()) {
                throw new AppException(ErrorCode.LABEL_NOT_FOUND);
            }
                annotation.setLabels(labels);
            }
        annotationRepository.save(annotation);
        reviewService.createReviews(annotation);

        return annotationMapper.toAnnotationResponse(annotation);
    }

    public void createAnnotation(Task task, List<Dataitem> dataitems){
       List<Annotation> annotations = new ArrayList<>();
       for (Dataitem dataitem: dataitems){
           Annotation annotation = new Annotation();
           annotation.setDataitem(dataitem);
           annotation.setTask(task);
           annotation.setUser(task.getAssignment().getAssignedTo());
           annotations.add(annotation);
       }
       annotationRepository.saveAll(annotations);
    }



    //get Status flow by taskitem
    public AnnotationStatus getAnnotationStatusFlowTaskItem(String taskDataItemId){
        TaskDataItem taskDataItem = taskDataItemRepository.findById(taskDataItemId)
                .orElseThrow(()-> new AppException(ErrorCode.TASKDATAITEM_NOT_FOUND));
        Annotation annotation = annotationRepository.findByTask_TaskIdAndDataitem_ItemId(
                taskDataItem.getTask().getTaskId(), taskDataItem.getDataitem().getItemId());

        return annotation.getAnnotationStatus();
    }


    // ================= REMOVE ANNOTATION BY ASSINGMENT_ID =================
    @LogActivity(
        action = "DELETE",
        entity = "Annotation",
        description = "Delete annotation",
        entityIdParam = "annotationId"
    )
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
            reviewService.removeReviewByAnnotation(annotation.getAnnotationId());
        }
        annotationRepository.saveAll(annotations);
    }

    //get annotation by dataitem
    public AnnotationResponse getAnnotationByDataItemId(String dataItemId){
        Dataitem dataitem = dataItemRepository.findById(dataItemId)
                .orElseThrow(()-> new AppException(ErrorCode.DATAITEM_NOT_FOUND));
        Annotation annotation = annotationRepository
                .findAnnotationByDataitem_ItemId(dataItemId);
        return annotationMapper.toAnnotationResponse(annotation);
    }
}
