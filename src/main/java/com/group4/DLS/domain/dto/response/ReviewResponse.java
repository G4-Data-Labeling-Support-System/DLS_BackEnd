package com.group4.DLS.domain.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.group4.DLS.domain.enums.ReviewStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewResponse {
    String reviewId;
    String annotationId;
    String reviewerId;
    ReviewStatus reviewStatus;
    String comment;
    LocalDateTime reviewedAt;
    List<String> evidences;
    AnnotationResponse annotation;
}
