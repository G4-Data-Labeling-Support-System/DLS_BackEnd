package com.group4.DLS.domain.dto.response;

import java.time.LocalDateTime;

import com.group4.DLS.domain.entity.enums.DataType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class DataItemResponse {
    String itemId;
    String datasetId;
    String fileName;
    String url;
    String fileFormat;
    int fileSize;
    int width;
    int height;
    DataType dataType;
    LocalDateTime uploadedAt;
    DatasetResponse dataset;
}
