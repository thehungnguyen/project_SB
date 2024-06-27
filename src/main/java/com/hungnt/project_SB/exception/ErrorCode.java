package com.hungnt.project_SB.exception;

public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized Error"),
    INVALID_KEY(1001, "Invalid message key"),
    USER_EXISTED(1002, "User Existed"),
    USERNAME_INVALID(1003, "Username must be at least 5 characters"),
    PASSWORD_INVALID(1004, "Password must be at least 8 characters"),
    USER_NOTFOUND(1005, "User not found")
    ;
    private int code;
    private String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
