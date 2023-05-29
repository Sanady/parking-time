package com.parkingtime.common.enums;

import lombok.Getter;

@Getter
public enum ErrorMessages {
    PASSWORD_MISMATCH("Passwords are not same, please check passwords again."),
    TOKEN_EXPIRED("Request token has expired, try again later."),
    ROLE_NOT_FOUND("Role not found."),
    USER_EMAIL_NOT_FOUND("User does not exists with this email"),
    CITY_ALREADY_EXISTS("City already exists"),
    USER_DOES_NOT_CONTAIN_SPECIFIC_ROLE("User does not contain specific role"),
    USER_ALREADY_EXISTS_IN_SYSTEM("User is already existing in system, please try again.");

    private final String value;

    ErrorMessages(String value) {
        this.value = value;
    }
}

