package com.group4.DLS.services;

import com.group4.DLS.domain.dto.request.ReviewCreationRequest;
import com.group4.DLS.domain.dto.response.ReviewResponse;
import com.group4.DLS.domain.entity.Annotation;
import com.group4.DLS.domain.entity.Review;
import com.group4.DLS.domain.entity.User;
import com.group4.DLS.exceptions.AppException;
import com.group4.DLS.exceptions.enums.ErrorCode;
import com.group4.DLS.mappers.ReviewMapper;
import com.group4.DLS.repositories.AnnotationRepository;
import com.group4.DLS.repositories.ReviewRepository;
import com.group4.DLS.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final AnnotationRepository annotationRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    public List<Review> createReview(ReviewCreationRequest request) {

        List<Review> responses = new ArrayList<>();

        for (String annotationId : request.getAnnotationIds()) {

            Annotation annotation = annotationRepository
                    .findById(annotationId)
                    .orElseThrow(() -> new AppException(ErrorCode.ANNOTATION_NOT_FOUND));

            User reviewer = userRepository
                    .findById(annotation.getTask().getAssignment().getReviewedBy().getUserId())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

            Review review = new Review();
            review.setAnnotation(annotation);
            review.setUser(reviewer);


            responses.add(review);
        }



        return reviewRepository.saveAll(responses);
    }
}