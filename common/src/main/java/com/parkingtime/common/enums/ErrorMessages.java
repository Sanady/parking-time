package com.parkingtime.common.enums;

import lombok.Getter;

@Getter
public enum ErrorMessages {
    PASSWORD_MISMATCH("Passwords are not same, please check passwords again."),
    TOKEN_EXPIRED("Request token has expired, try again later."),
    ROLE_NOT_FOUND("Role not found."),
    USER_EMAIL_NOT_FOUND("User does not exists with this email!"),
    CITY_ALREADY_EXISTS("City already exists"),
    USER_EMAIL_DOES_NOT_HAVE_EMAIL_VERIFIED("User with this email does not have email verification!"),
    NOT_VALID_EMAIL("Email is not valid"),
    USER_OLD_PASSWORD_DOES_NOT_MATCH("User old password does not match with the password from the request!"),
    USER_NEW_PASSWORD_DOES_NOT_MATCH("User new password and confirmation password does not matches!"),
    USER_DOES_NOT_CONTAIN_SPECIFIC_ROLE("User does not contain specific role"),
    USER_ALREADY_EXISTS_IN_SYSTEM("User is already existing in system, please try again."),
    VEHICLE_NOT_FOUND("Vehicle not found");

    private final String value;

    ErrorMessages(String value) {
        this.value = value;
    }
}

