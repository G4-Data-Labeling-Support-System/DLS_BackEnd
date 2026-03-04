package com.group4.DLS.domain.entity.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum AssignmentStatus {



    ASSIGNED, //Assignment đã được giao cho annotator
    IN_PROGRESS, //Annotator đang thực hiện annotation trên assignment
    REVIEWING, //Assignment đã được annotator hoàn thành và đang chờ reviewer đánh giá
    COMPLETED, //Assignment đã được reviewer đánh giá và hoàn thành
    CANCLED; //Assignment đã bị hủy bỏ

    @JsonCreator
    public static AssignmentStatus fromString(String value) {
        return AssignmentStatus.valueOf(value.toUpperCase());
    }
}
