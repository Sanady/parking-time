package com.parkingtime.common.enums;

import lombok.Getter;

@Getter
public enum MessageEnum {
    SUCCESSFULLY_RESET_PASSWORD("You have successfully reset password, try log in with new password."),
    CITY_SUCCESSFULLY_ADDED("City has been successfully added"),
    ROLES_ADDED("Roles have been successfully added"),
    ROLE_REMOVED("Role has been removed from a user"),
    PLEASE_CHECK_INBOX("Please check your email inbox.");

    private final String value;

    MessageEnum(String value) {
        this.value = value;
    }
}
