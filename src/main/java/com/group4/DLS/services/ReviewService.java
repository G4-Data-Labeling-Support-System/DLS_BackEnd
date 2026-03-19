package com.group4.DLS.services;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import org.springframework.stereotype.Service;

import com.group4.DLS.domain.entity.Review;
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
    public void createReviews(Task task) {
        List<Annotation> annotations = annotationRepository.findAnnotationsByTask(task);

        User reviewer = task.getAssignment().getReviewedBy();
        if (annotations.isEmpty()) {
            throw new AppException(ErrorCode.ANNOTATIONS_NOT_HAVE);
        }
        List<Review> reviewsToSave = new ArrayList<>();

        for (Annotation annotation : annotations) {
           if(annotation.getAnnotationStatus().equals(AnnotationStatus.SUBMITTED)){
               // Tạo review lần đầu cho annotation
               Review review = new Review();
               review.setComment(""); // comment rỗng lần đầu
               review.setUser(reviewer); // map reviewer
               review.setAnnotation(annotation); // map annotation
               review.setEvidences(new ArrayList<>()); // tạo list evidences rỗng

               reviewsToSave.add(review);
           }
        }

        // Lưu tất cả review vào database
        reviewRepository.saveAll(reviewsToSave);

    }

    public List<ReviewResponse> isReviewing(ReviewUpdateRequest request) throws IOException {
        List<Review> reviews = new ArrayList<>();

        for (ReviewItemRequest item : request.getReviews()) {

            Annotation annotation = annotationRepository.findById(item.getAnnotationId())
                    .orElseThrow(() -> new AppException(ErrorCode.ANNOTATION_NOT_FOUND));

            Review review = reviewRepository.findByAnnotation_AnnotationId(annotation.getAnnotationId());

            review.setReviewedAt(LocalDateTime.now());//set time
            review.setComment(item.getComment()); // set comment
            review.setReviewStatus(item.getReviewStatus());
            List<String> envidences = new ArrayList<>();
            for(MultipartFile url: item.getEnvidence()){
                envidences.add(seaweedFilerUploadService.uploadImage(url, "Envidence"));//tạo chuỗi string ảnh
            }
            review.setEvidences(envidences);

            //số annotation được approved
            if(item.getReviewStatus().equals(ReviewStatus.APPROVED)){
                Task task = annotation.getTask();
                task.setCompletedCount(task.getCompletedCount()+1);
            }

            reviews.add(review);
        }
        reviewRepository.saveAll(reviews);

        return reviewMapper.toReviewResponse(reviews);

    }

}
