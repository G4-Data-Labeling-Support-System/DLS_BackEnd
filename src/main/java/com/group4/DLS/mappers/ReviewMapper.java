package com.group4.DLS.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.group4.DLS.domain.dto.request.ReviewCreationRequest;
import com.group4.DLS.domain.dto.response.ReviewResponse;
import com.group4.DLS.domain.entity.Annotation;
import com.group4.DLS.domain.entity.Review;
import com.group4.DLS.domain.entity.User;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(target = "reviewId", ignore = true)
    @Mapping(target = "reviewedAt", ignore = true)
    ReviewResponse toReview(ReviewCreationRequest request);

    @Mapping(target = "annotationId", source = "annotation.annotationId")
    @Mapping(target = "reviewerId", source = "user.userId")
    ReviewResponse toReviewResponse(Review review);
}