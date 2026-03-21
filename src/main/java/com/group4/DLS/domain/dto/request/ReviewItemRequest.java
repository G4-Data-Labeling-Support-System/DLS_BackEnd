package com.group4.DLS.domain.dto.request;

import com.group4.DLS.domain.enums.ReviewStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ReviewItemRequest {
    String annotationId;
    String comment;
    ReviewStatus reviewStatus;
    List<MultipartFile> envidence;
}
