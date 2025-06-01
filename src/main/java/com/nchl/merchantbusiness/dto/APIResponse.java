package com.nchl.merchantbusiness.dto;

import com.nchl.merchantbusiness.constant.ResponseDetail;
import com.nchl.merchantbusiness.entity.InputFieldError;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@Builder
public class APIResponse<T> {

    private String code;
    private String status;
    private String message;
    private String timeStamp;
    private T data;
    private List<InputFieldError> errors;

    public static <T> APIResponse<T> apiResponse(ResponseDetail apiCode, String message, T responseBody) {

        return APIResponse.<T>builder()
                .code(apiCode.getRespCode())
                .status(apiCode.getRespStatus())
                .message(message)
                .timeStamp(String.valueOf(LocalDateTime.now()))
                .data(responseBody)
                .errors(Collections.emptyList())
                .build();
    }

    public static <T> APIResponse<T> apiResponse(ResponseDetail apiCode, String message, T responseBody,
                                                 List<InputFieldError> errors) {

        return APIResponse.<T>builder()
                .code(apiCode.getRespCode())
                .status(apiCode.getRespStatus())
                .message(message)
                .timeStamp(String.valueOf(LocalDateTime.now()))
                .data(responseBody)
                .errors(errors)
                .build();
    }
}