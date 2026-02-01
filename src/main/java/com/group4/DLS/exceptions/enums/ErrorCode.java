package com.group4.DLS.exceptions.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@RequiredArgsConstructor
public enum ErrorCode {
    UNCATEGORIZED(999, "Uncategorized error"),
    
    USER_NOT_FOUND(404, "User not found"),
    USER_EXISTS(409, "User already exists"),
    USER_NOT_ACTIVE(410, "User not activated"),

    INVALID_USERNAME_LENGTH(400, "Invalid username length"),
    INVALID_FULLNAME_LENGTH(400, "Invalid fullname length"),
    INVALID_PASSWORD_LENGTH(400, "Password must be 8-16 characters"),
    INVALID_OLD_PASSWORD(400, "Invalid old password"),
    NEW_PASSWORD_SAME_AS_OLD(400, "New password same as old"),
    PASSWORD_CONFIRM_NOT_MATCH(400, "Confirm password not match"),

    INVALID_CREDENTIALS(403, "Invalid credentials"),

    INVALID_EMAIL_FORMAT(400, "Invalid email format"),
    FORBIDDEN(403, "Access denied"),
    PROJECT_NOT_FOUND(404, "Project not found"),
    DATASET_NOT_FOUND(404, "Dataset not found"),

    INTERNAL_SERVER_ERROR(500, "Internal server error"),
    ASSIGNMENT_EXISTS(409,"Assignment name already exists" ),
    ASSIGNMENT_NOT_FOUND(400, "Assignment not found"),
    INVALID_ASSIGNMENT_STATUS(400, "Invalid status assignment"),
    GUIDELINE_NOT_FOUND(400,"Guideline not found" ),
    GUIDELINE_EXISTS(400,"Exist Guideline Name" );

    final int code;
    final String message;
}
