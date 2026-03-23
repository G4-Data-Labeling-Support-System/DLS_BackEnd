package com.group4.DLS.domain.dto.response;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

import com.group4.DLS.domain.enums.LabelStatus;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LabelResponse {

    String labelId;
    String datasetId;
    String labelName;
    String color;
    String description;
    LabelStatus labelStatus;
    LocalDateTime createdAt;
}