package com.parkingtime.common.enums;

import lombok.Getter;

@Getter
public enum RoleEnum {
    ROLE_USER("ROLE_USER"),
    ROLE_MODERATOR("ROLE_MODERATOR"),
    ROLE_ADMIN("ROLE_ADMIN");

    private final String value;

    RoleEnum(String value) {
        this.value = value;
    }
}

