package com.group4.DLS.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.group4.DLS.domain.dto.response.ApiResponse;
import com.group4.DLS.exceptions.enums.ErrorCode;

@RestControllerAdvice
public class GlobalExceptionHandler {
    // Handle general exceptions here
    // More specific exception handlers can be added here
    @ExceptionHandler(value = AppException.class)
    public ResponseEntity<ApiResponse<String>> handleAppException(AppException ex) {
        ApiResponse<String> response = new ApiResponse<>();
        ErrorCode errorCode = ex.getErrorCode();

        response.setCode(errorCode.getCode());
        response.setMessage(errorCode.getMessage());
        response.setData(null);

        return ResponseEntity
                .badRequest()
                .body(response);
    }

    // Handle validation exceptions here
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        ApiResponse<String> response = new ApiResponse<>();

        String enumString = ex.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.valueOf(enumString);

        response.setCode(errorCode.getCode());
        response.setMessage(errorCode.getMessage());
        response.setData(null);

        return ResponseEntity
                .badRequest()
                .body(response);
    }

    // Fallback for uncategorized errors
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiResponse<String>> handleRuntimeException(Exception ex) {
        ApiResponse<String> response = new ApiResponse<>();
        ErrorCode errorCode = ErrorCode.UNCATEGORIZED;

        response.setCode(errorCode.getCode());
        response.setMessage(errorCode.getMessage());
        response.setData(null);

        return ResponseEntity
                .badRequest()
                .body(response);
    }
}