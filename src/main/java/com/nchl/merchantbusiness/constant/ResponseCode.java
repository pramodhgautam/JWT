package com.nchl.merchantbusiness.constant;

public enum ResponseCode {
    SUCCESS("000", "Operation successful"),
    FAILURE("001", "Operation failed"),
    VALIDATION_ERROR("002", "Validation error"),
    UNAUTHORIZED("003", "Authentication required"),
    FORBIDDEN("004", "Access denied"),
    NOT_FOUND("005", "Resource not found"),
    CONFLICT("006", "Resource conflict"),
    SERVER_ERROR("007", "Internal server error");

    private final String code;
    private final String defaultMessage;

    ResponseCode(String code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    public String getCode() {
        return code;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
}