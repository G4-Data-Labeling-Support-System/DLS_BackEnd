package com.group4.DLS.domain.dto.request;

import com.group4.DLS.domain.enums.ReviewStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class ReviewItemRequest {
    String annotationId;
    String comment;
    ReviewStatus reviewStatus;
    List<MultipartFile> envidence;
}
