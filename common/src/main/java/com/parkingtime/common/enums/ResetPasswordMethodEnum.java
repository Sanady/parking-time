package com.parkingtime.common.enums;

import lombok.Getter;

@Getter
public enum ResetPasswordMethodEnum {
    METHOD_EMAIL("email");

    private final String value;

    ResetPasswordMethodEnum(String value) {
        this.value = value;
    }
}
