package com.nchl.merchantbusiness.constant;

public enum ResponseDetail {
    SUCCESS("000", "Operation successful"),
    FAILURE("001", "Operation failed"),
    VALIDATION_ERROR("002", "Validation error"),
    UNAUTHORIZED("003", "Authentication required"),
    FORBIDDEN("004", "Access denied"),
    NOT_FOUND("005", "Resource not found"),
    CONFLICT("006", "Resource conflict"),
    SERVER_ERROR("007", "Internal server error");

    private final String respCode;
    private final String respStatus;

    ResponseDetail(String respCode, String respStatus) {
        this.respCode = respCode;
        this.respStatus = respStatus;
    }

    public String getRespCode() {
        return respStatus;
    }

    public String getRespStatus() {
        return respCode;
    }
}