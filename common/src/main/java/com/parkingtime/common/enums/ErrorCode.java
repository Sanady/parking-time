package com.parkingtime.common.enums;

import lombok.Getter;

@Getter
public enum ErrorCode {
    INVALID_REQUEST_DATA("APP_0001", "Invalid request data"),
    CONSTRAINT_VIOLATION("APP_0002", "Constraint violation"),
    ILLEGAL_ARGUMENT("APP_0003", "Illegal argument"),
    NOT_FOUND("APP_0004", "Not found"),
    ALREADY_EXISTS("APP_0005", "Conflict"),
    USER_CONFLICT_EXCEPTION("AUTH_0001", "User already exists"),
    UNAUTHORIZED("AUTH_0002", "Authentication failed");


    private final String code;
    private final String title;

    ErrorCode(String code, String title) {
        this.code = code;
        this.title = title;
    }
}
