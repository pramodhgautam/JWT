package com.nchl.merchantbusiness.dto;

import com.nchl.merchantbusiness.model.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private String responseCode;
    private String responseStatus;
    private String responseMessage;
    private T data;
    private Instant timestamp;
    private String errors;

    // Success static factory methods
    public static <T> ApiResponse<T> success(T data) {
        return success(data, ResponseCode.SUCCESS.getDefaultMessage());
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .responseCode(ResponseCode.SUCCESS.getCode())
                .responseMessage(message)
                .data(data)
                .timestamp(Instant.now())
                .build();
    }

    // Failure static factory methods
    public static ApiResponse<?> failure(ResponseCode code) {
        return failure(code, code.getDefaultMessage());
    }

    public static ApiResponse<?> failure(ResponseCode code, String message) {
        return ApiResponse.builder()
                .responseCode(code.getCode())
                .responseMessage(message)
                .data(null)
                .timestamp(Instant.now())
                .build();
    }

    // Convenience methods for common scenarios
    public static ApiResponse<?> validationError(String message) {
        return failure(ResponseCode.VALIDATION_ERROR, message);
    }

    public static ApiResponse<?> notFound(String resourceName) {
        return failure(ResponseCode.NOT_FOUND,
                resourceName + " not found");
    }
}