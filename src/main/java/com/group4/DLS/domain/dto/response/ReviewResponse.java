package com.group4.DLS.domain.dto.response;


import java.time.LocalDateTime;

import com.group4.DLS.domain.enums.ReviewStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ReviewResponse {

    String reviewId;

    String annotationId;

    String reviewerId;

    ReviewStatus reviewStatus;

    String comment;

    LocalDateTime reviewedAt;
}