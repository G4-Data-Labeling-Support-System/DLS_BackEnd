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

    // 403 errors
    USER_NOT_ACTIVE(403, "User is not active"),
    FORBIDDEN(403, "Access denied"),
    INVALID_CREDENTIALS(403, "Invalid credentials"),

    // 409 errors
    USER_EXISTS(409, "User already exists"),
    ASSIGNMENT_EXISTS(409,"Assignment name already exists" ),

    // 400 errors
    INVALID_USERNAME_LENGTH(400, "Invalid username length"),
    INVALID_FULLNAME_LENGTH(400, "Invalid fullname length"),
    INVALID_EMAIL_FORMAT(400, "Invalid email format"),
    INVALID_PASSWORD_LENGTH(400, "Password must be 8-16 characters"),
    INVALID_OLD_PASSWORD(400, "Invalid old password"),
    NEW_PASSWORD_SAME_AS_OLD(400, "New password same as old"),
    PASSWORD_CONFIRM_NOT_MATCH(400, "Confirm password not match"),
    INVALID_ASSIGNMENT_STATUS(400, "Invalid status assignment"),

    INTERNAL_SERVER_ERROR(500, "Internal server error");

    final int code;
    final String message;
}
