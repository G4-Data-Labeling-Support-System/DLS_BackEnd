package com.group4.DLS.services;

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

import org.springframework.stereotype.Service;

import com.group4.DLS.domain.entity.Review;
import com.group4.DLS.repositories.AnnotationRepository;
import com.group4.DLS.repositories.ReviewRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ReviewService {
    ReviewRepository reviewRepository;
    AnnotationRepository annotationRepository;

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

    public List<ReviewResponse> isReviewing(ReviewUpdateRequest request){
        List<ReviewResponse> reviews = new ArrayList<>();
        for (ReviewItemRequest item : request) {


        }
    }

}
