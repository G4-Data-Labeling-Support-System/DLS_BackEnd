package com.group4.DLS.domain.dto.request;


import com.group4.DLS.domain.entity.Annotation;
import com.group4.DLS.domain.enums.ReviewStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ReviewCreationRequest {
     List<String> annotationIds;
}