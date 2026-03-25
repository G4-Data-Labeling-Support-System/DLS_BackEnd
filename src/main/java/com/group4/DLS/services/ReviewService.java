package com.group4.DLS.services;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.group4.DLS.aop.LogActivity;
import com.group4.DLS.domain.dto.request.ReviewItemRequest;
import com.group4.DLS.domain.dto.request.ReviewUpdateRequest;
import com.group4.DLS.domain.dto.response.ReviewResponse;
import com.group4.DLS.domain.entity.Annotation;
import com.group4.DLS.domain.entity.Review;
import com.group4.DLS.domain.entity.Task;
import com.group4.DLS.domain.entity.User;
import com.group4.DLS.domain.enums.AnnotationStatus;
import com.group4.DLS.domain.enums.ReviewStatus;
import com.group4.DLS.exceptions.AppException;
import com.group4.DLS.exceptions.enums.ErrorCode;

import com.group4.DLS.mappers.ReviewMapper;
import com.group4.DLS.repositories.TaskRepository;
import org.springframework.stereotype.Service;

import com.group4.DLS.repositories.AnnotationRepository;
import com.group4.DLS.repositories.ReviewRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ReviewService {
    ReviewRepository reviewRepository;
    AnnotationRepository annotationRepository;
    SeaweedFilerUploadService seaweedFilerUploadService;
    ReviewMapper reviewMapper;
    private final TaskRepository taskRepository;

    // ================= REMOVE REVIEW BY ANNOTATION_ID =================
    @Transactional
    public void removeReviewByAnnotationId(List<String> annotationIds) {

        // Get all reviews for current annotation
        List<Review> reviews = reviewRepository.findByAnnotation_AnnotationIdIn(annotationIds);

        for (Review review : reviews) {
            review.setReviewStatus(ReviewStatus.INACTIVE);
        }

        reviewRepository.saveAll(reviews);
    }

    // create when flagForReview of task is true;
    @LogActivity(
            action = "CREATE",
            entity = "Review",
            description = "Create Reviews",
            entityIdField = "taskId"
    )
    public void createReviews(Task task) {
        List<Annotation> annotations = annotationRepository.findAnnotationsByTask(task);

        User reviewer = task.getAssignment().getReviewedBy();
        if (annotations.isEmpty()) {
            throw new AppException(ErrorCode.ANNOTATIONS_NOT_HAVE);
        }
        List<Review> reviewsToSave = new ArrayList<>();

        for (Annotation annotation : annotations) {
           if(annotation.getAnnotationStatus().equals(AnnotationStatus.SUBMITTED)){
               // lấy review mới nhất
               Review latestReview = reviewRepository
                       .findTopByAnnotation_AnnotationIdOrderByCreatedAtDesc(annotation.getAnnotationId());

               // nếu chưa có review hoặc review trước đã xử lý xong
               if (latestReview == null || latestReview.getReviewStatus() == ReviewStatus.REJECTED) {

                   Review review = new Review();
                   review.setComment("");
                   review.setUser(reviewer);
                   review.setAnnotation(annotation);
                   review.setEvidences(new ArrayList<>());
                   review.setReviewStatus(ReviewStatus.IN_PROGRESS); //  quan trọng

                   reviewsToSave.add(review);
               }
           }
        }

        // Lưu tất cả review vào database
        reviewRepository.saveAll(reviewsToSave);

    }

    //after reviewer reviews success
    @LogActivity(
            action = "UPDATE",
            entity = "Review",
            description = "Update view after reviewer reviews",
            entityIdParam = "reviewerId"
    )
    public List<ReviewResponse> reviewed(ReviewUpdateRequest request, List<MultipartFile> files) throws IOException {

        Task task = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new AppException(ErrorCode.TASK_NOT_FOUND));

        int countCompleted = 0;

        List<Review> reviews = new ArrayList<>();
        List<Annotation> annotations = new ArrayList<>();

        for (ReviewItemRequest item : request.getReviews()) {

            Annotation annotation = annotationRepository.findById(item.getAnnotationId())
                    .orElseThrow(() -> new AppException(ErrorCode.ANNOTATION_NOT_FOUND));

            Review review = reviewRepository
                    .findTopByAnnotation_AnnotationIdOrderByCreatedAtDesc(annotation.getAnnotationId());

            if (review == null) {
                throw new AppException(ErrorCode.REVIEW_NOT_FOUND);
            }

            review.setReviewedAt(LocalDateTime.now());
            review.setComment(item.getComment());
            review.setReviewStatus(ReviewStatus.valueOf(item.getReviewStatus()));

            // xử lý file theo index
            List<String> evidences = new ArrayList<>();

            if (item.getFileIndexes() != null && files != null) {
                for (Integer index : item.getFileIndexes()) {

                    if (index < files.size()) {
                        MultipartFile file = files.get(index);

                        String url = seaweedFilerUploadService.uploadImage(file, "Evidence");
                        evidences.add(url);
                    }
                }
            }

            review.setEvidences(evidences);

            // update status
            String status = item.getReviewStatus();

            if (AnnotationStatus.valueOf(status) == AnnotationStatus.APPROVED) {
                countCompleted++;
            }

            annotation.setAnnotationStatus(AnnotationStatus.valueOf(status));

            annotations.add(annotation);
            reviews.add(review);
        }

        annotationRepository.saveAll(annotations);
        reviewRepository.saveAll(reviews);

        task.setCompletedCount(countCompleted);
        taskRepository.save(task);

        return reviewMapper.toReviewResponse(reviews);
    }

    //get reviews by AnnotationId
    public List<ReviewResponse> reviewsOfAnntation(String annotationId){
        List<Review> reviews = reviewRepository.findByAnnotation_AnnotationId(annotationId);
        return reviewMapper.toReviewResponse(reviews);
    }

    @LogActivity(
            action = "DELETE",
            entity = "Annotation",
            description = "Delete Annotation",
            entityIdParam = "annotationId"
    )
    public void removeReviewByAnnotation(String annotationId){
        Annotation annotation = annotationRepository.findById(annotationId)
                .orElseThrow(()-> new AppException(ErrorCode.ANNOTATION_NOT_FOUND));

        List<Review> reviews = annotation.getReviews();

        for(Review review: reviews){
            review.setReviewStatus(ReviewStatus.INACTIVE);
        }

        reviewRepository.saveAll(reviews);
    }

}
