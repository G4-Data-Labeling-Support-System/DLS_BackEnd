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

    INVALID_USERNAME_LENGTH(400, "Invalid username length"),
    INVALID_FULLNAME_LENGTH(400, "Invalid fullname length"),
    INVALID_PASSWORD(400, "Invalid password"),
    INVALID_EMAIL_FORMAT(400, "Invalid email format"),

    INTERNAL_SERVER_ERROR(500, "Internal server error");

    final int code;
    final String message;
}
