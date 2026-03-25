package com.group4.DLS.domain.dto.request;

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
public class ReviewUpdateRequest {
    String taskId;
    String annotationId;
    String comment;
    String reviewStatus;
    List<MultipartFile> envidence;
}
