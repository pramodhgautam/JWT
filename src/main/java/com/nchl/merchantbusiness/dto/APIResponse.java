package com.nchl.merchantbusiness.dto;

import com.nchl.merchantbusiness.constant.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class APIResponse<T> {
    private String code;
    private String status;
    private String message;
    private Instant timestamp;
    private T data;
    private String errors;

    public static <T> APIResponse<T> success(T data, String message) {
        return APIResponse.<T>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .message(message)
                .data(data)
                .timestamp(Instant.now())
                .build();
    }

    public static APIResponse<?> failure(ResponseCode code, String message) {
        return APIResponse.builder()
                .code(code.getCode())
                .message(message)
                .data(null)
                .timestamp(Instant.now())
                .build();
    }


    public static APIResponse<?> validationError(String message) {
        return failure(ResponseCode.VALIDATION_ERROR, message);
    }

    public static APIResponse<?> notFound(String resourceName) {
        return failure(ResponseCode.NOT_FOUND,
                resourceName + " not found");
    }
}