package com.group4.DLS.services;

import com.group4.DLS.domain.dto.request.ReviewCreationRequest;
import com.group4.DLS.domain.dto.response.ReviewResponse;
import com.group4.DLS.domain.entity.Annotation;
import com.group4.DLS.domain.entity.Review;
import com.group4.DLS.domain.entity.Task;
import com.group4.DLS.domain.entity.User;
import com.group4.DLS.exceptions.AppException;
import com.group4.DLS.exceptions.enums.ErrorCode;
import com.group4.DLS.mappers.ReviewMapper;
import com.group4.DLS.repositories.AnnotationRepository;
import com.group4.DLS.repositories.ReviewRepository;
import com.group4.DLS.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final AnnotationRepository annotationRepository;
    private final UserRepository userRepository;
    private final ReviewMapper reviewMapper;

    /*
    *CREATE REVIEW
     */
    public ReviewResponse createReview(ReviewCreationRequest request) {

        Annotation annotation = annotationRepository.findById(request.getAnnotationId())
                .orElseThrow(() -> new AppException(ErrorCode.ANNOTATION_NOT_FOUND));


        Task task = annotation.getTask();
        if (!task.isFlagForReview ) {
            throw new AppException(ErrorCode.TASK_NOT_READY_FOR_REVIEW);
        }
    }



    public ReviewResponse createReview(ReviewCreationRequest request){

        Annotation annotation = annotationRepository.findById(request.getAnnotationId())
                .orElseThrow(() -> new AppException(ErrorCode.ANNOTATION_NOT_FOUND));

        Task task = annotation.getTask();

        if(!task.is){
            throw new AppException(ErrorCode.TASK_NOT_READY_FOR_REVIEW);
        }

        User reviewer = currentUserProvider.getCurrentUser();

        Review review = Review.builder()
                .annotation(annotation)
                .reviewer(reviewer)
                .reviewStatus(request.getReviewStatus())
                .evidence(request.getEvidence())
                .comment(request.getComment())
                .build();

        reviewRepository.save(review);

        return reviewMapper.toResponse(review);
    }
    /*
     * REVIEW ANNOTATION
     */
    public ReviewResponse reviewAnnotation(String annotationId, ReviewCreationRequest request) {

        Annotation annotation = annotationRepository
                .findById(annotationId)
                .orElseThrow(() -> new AppException(ErrorCode.ANNOTATION_NOT_FOUND));

        User reviewer = userRepository
                .findById(request.getReviewerId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Review review = reviewMapper.toReview(request, reviewer, annotation);

        reviewRepository.save(review);

        return reviewMapper.toReviewResponse(review);
    }

    /*
     * GET REVIEWS BY ANNOTATION
     */
    public List<ReviewResponse> getReviewsByAnnotation(String annotationId) {

        List<Review> reviews = reviewRepository.findByAnnotation_AnnotationId(annotationId);

        return reviews.stream()
                .map(reviewMapper::toReviewResponse)
                .toList();
    }
}