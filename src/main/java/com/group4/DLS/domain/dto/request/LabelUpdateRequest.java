package com.group4.DLS.domain.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LabelUpdateRequest {

    @NotBlank(message = "Label name must not be blank")
    @Size(min = 2, max = 100, message = "Label name must be between 2 and 100 characters")
    String labelName;

    @NotBlank(message = "Color must not be blank")
    @Pattern(
            regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$",
            message = "Color must be a valid HEX code (e.g. #FF5733 or #FFF)"
    )
    String color;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    String description;
}