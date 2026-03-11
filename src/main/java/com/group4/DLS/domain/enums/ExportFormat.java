package com.group4.DLS.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ExportFormat {
    COCO,
    YOLO,
    VOC,
    JSON;

    @JsonCreator
    public static ExportFormat fromString(String value) {
        return ExportFormat.valueOf(value.toUpperCase());
    }
}
