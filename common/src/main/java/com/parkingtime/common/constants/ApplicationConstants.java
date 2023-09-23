package com.parkingtime.common.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplicationConstants {
    // REGEX
    public static final String EMAIL_REGEX = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
    public static final String ONLY_CHARACTERS = "^[A-Z][a-z]+$";
    public static final String CITY_REGEX = "^[a-zA-Z]+(?:[\\s-][a-zA-Z]+)*$";
    // TIME
    public static final String DELAY_DATABASE_PASSWORD_TOKEN_CLEAN_UP = "0 0/30 * * * ?"; // 6 Hours

    // URL
    public static final String API = "/api";
    public static final String V1 = API + "/v1";

    public static final String AUTH = V1 + "/auth";
    public static final String REGISTRATION = "/registration";
    public static final String AUTHENTICATE = "/authenticate";
    public static final String FORGET_PASSWORD = "/forget-password";
    public static final String RESET_PASSWORD = "/reset-password";
    public static final String EMAIL_VERIFICATION = "/email-verification";
    public static final String VERIFY_EMAIL = "/verify-email";

    public static final String USER = V1 + "/user";
    public static final String GET_USER = "/{email}";
    public static final String CHANGE_USER_PASSWORD = GET_USER + "/change-password";

    public static final String VEHICLE = V1 + "/vehicle";

    public static final String CITY = V1 + "/city";

}
