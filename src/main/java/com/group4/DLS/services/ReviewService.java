package com.group4.DLS.services;

import com.group4.DLS.domain.dto.request.ReviewCreationRequest;
import com.group4.DLS.domain.dto.response.ReviewResponse;
import com.group4.DLS.domain.entity.Annotation;
import com.group4.DLS.domain.entity.Review;
import com.group4.DLS.domain.entity.User;
import com.group4.DLS.exceptions.AppException;
import com.group4.DLS.mappers.ReviewMapper;
import com.group4.DLS.repositories.AnnotationRepository;
import com.group4.DLS.repositories.ReviewRepository;
import com.group4.DLS.repositories.UserRepository;
import com.group4.DLS.exceptions.enums.ErrorCode;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final AnnotationRepository annotationRepository;
    private final UserRepository userRepository;
    private final ReviewMapper reviewMapper;

    public ReviewResponse createReview(ReviewCreationRequest request){

        Annotation annotation = annotationRepository.findById(request.getAnnotationId())
                .orElseThrow(() -> new AppException(ErrorCode.ANNOTATION_NOT_FOUND));

        User reviewer = userRepository.findById(request.getReviewerId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Review review = reviewMapper.toReview(request, reviewer, annotation);

        reviewRepository.save(review);

        return reviewMapper.toReviewResponse(review);
    }
}