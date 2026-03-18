package com.group4.DLS.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.group4.DLS.domain.entity.Review;
import com.group4.DLS.exceptions.AppException;
import com.group4.DLS.exceptions.enums.ErrorCode;
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

    // ================= REMOVE REVIEW BY ANNOTATION_ID =================
    @Transactional
    public void removeReviewByAnnotationId(List<String> annotationIds) {

        // Get all reviews for current annotation
        // List<Review> reviews = reviewRepository.findByAnnotation_AnnotationId(annotationIds);

        reviewRepository.deleteByAnnotation_AnnotationIdIn(annotationIds);
    }
}
