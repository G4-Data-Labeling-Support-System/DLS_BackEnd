package com.group4.DLS.domain.dto.response;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LabelResponse {

    String labelId;
    String datasetId;
    String labelName;
    String color;
    String description;
    LocalDateTime createdAt;
}