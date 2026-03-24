package com.group4.DLS.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class DatasetUpdateRequest {
    @NotNull(message = "PROJECT_IS_REQUIRED")
    private String projectId;

    @Size(min = 3, max = 255, message = "INVALID_DATASET_NAME_LENGTH")
    private String datasetName;

    @Size(max = 1000, message = "INVALID_DATASET_DESCRIPTION_LENGTH")
    private String description;

    // FIX ARRAY STRING
    @Schema(type = "array", example = "[\"id1\",\"id2\"]")
    private List<String> deleteDataItemId;

    // FIX FILE UPLOAD (QUAN TRỌNG)
    @Schema(type = "string", format = "binary")
    private MultipartFile[] files;
}
