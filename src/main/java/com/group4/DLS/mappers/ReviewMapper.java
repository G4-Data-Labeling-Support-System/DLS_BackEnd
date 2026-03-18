package com.group4.DLS.mappers;

import java.util.List;

import com.group4.DLS.domain.entity.Annotation;
import com.group4.DLS.domain.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.group4.DLS.domain.dto.response.ReviewResponse;
import com.group4.DLS.domain.entity.Review;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    
    @Mapping(source = "annotation.annotationId", target = "annotationId")
    @Mapping(source = "user.userId", target = "reviewerId")
    ReviewResponse toReviewResponse(Review review);

    List<ReviewResponse> toReviewResponse(List<Review> reviews);




}
