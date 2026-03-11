package com.group4.DLS.exceptions.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@RequiredArgsConstructor
public enum ErrorCode {
    UNCATEGORIZED(999, "Uncategorized error"),

    // 404 errors
    USER_NOT_FOUND(404, "User not found"),
    PROJECT_NOT_FOUND(404, "Project not found"),
    ASSIGNMENT_NOT_FOUND(404, "Assignment not found"),
    DATASET_NOT_FOUND(404, "Dataset not found"),
    GUIDELINE_NOT_FOUND(404,"Guideline not found" ),

    // 403 errors
    USER_NOT_ACTIVE(403, "User is not active"),
    USER_ALREADY_ACTIVE(403, "User already active"),
    USER_ALREADY_INACTIVE(403, "User already InActive"),
    FORBIDDEN(403, "Access denied"),
    INVALID_CREDENTIALS(403, "Invalid credentials"),
    USER_NOT_MANAGER(403, "User is not a manager"),

    // 409 errors
    USER_EXISTS(409, "User already exists"),
    ASSIGNMENT_EXISTS(409,"Assignment name already exists" ),
    GUIDELINE_EXISTS(409,"Exist Guideline Name" ),
    PROJECT_ALREADY_EXISTS(409, "Project already exists"),
    DATASET_ALREADY_EXISTS(409, "Dataset already exists"),

    // 400 errors
    INVALID_USERNAME_LENGTH(400, "Invalid username length"),
    INVALID_FULLNAME_LENGTH(400, "Invalid fullname length"),
    INVALID_EMAIL_FORMAT(400, "Invalid email format"),
    INVALID_PASSWORD_LENGTH(400, "Password must be 8-16 characters"),
    INVALID_OLD_PASSWORD(400, "Invalid old password"),
    INVALID_DATASET_NAME_LENGTH(400, "Dataset Name must be in range 3-100 characters"),
    INVALID_DATASET_DESCRIPTION_LENGTH(400, "Dataset Description must in range 0-500 characters"),
    NEW_PASSWORD_SAME_AS_OLD(400, "New password same as old"),
    PASSWORD_CONFIRM_NOT_MATCH(400, "Confirm password not match"),
    INVALID_ASSIGNMENT_STATUS(400, "Invalid status assignment"),
    REQUIRE_PROJECT_ID(400, "Project Id is needed"),
    DATASETNAME_CANNOT_BE_NULL(400, "Dataset name cannot be null"),
    DATASETNAME_ALREADY_EXSITS(400, "Dataset name already exists"),

    LABEL_NOT_FOUND(3001, "Label not found"),
    LABEL_ALREADY_EXISTS(3002, "Label already exists in this dataset"),
    LABEL_HAS_ANNOTATIONS(3003, "Cannot delete label because it is used in annotations"),
    OVER_SIZE_FILE(413, "File size exceeds the maximum limit of 5MB"),
    INVALID_IMAGE_FILE(403,"Invalid image file" ),
    INVALID_FILE_FORMAT(403,"Invalid file format" ),
    DATAITEM_NOT_FOUND(403,"DataItem not found" ),
    TASK_NOT_FOUND(400, "Task not found"),
    TASK_ALREADY_EXISTS(400, "Task already exists"),
    INVALID_TASK_STATUS(400, "Invalid task status");

    final int code;
    final String message;
}
