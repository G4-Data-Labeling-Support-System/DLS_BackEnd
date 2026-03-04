package com.group4.DLS.domain.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LabelCreationRequest {

    @NotBlank
    String datasetId;

    @NotBlank
    String labelName;

    String color;
    String description;
}