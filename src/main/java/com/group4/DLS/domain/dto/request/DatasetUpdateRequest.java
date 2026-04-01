package com.group4.DLS.domain.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @NotNull(message = "project id is required")
    private String projectId;

    @Size(min = 3, max = 255, message = "dataset name must be between 3 and 255 characters")
    private String datasetName;

    @Size(max = 1000, message = "description must not exceed 1000 characters")
    private String description;

    @Schema(type = "array", example = "[\"id1\",\"id2\"]")
    private List<String> deleteDataItemId;

}
