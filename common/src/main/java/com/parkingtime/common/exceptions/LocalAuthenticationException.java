package com.parkingtime.common.exceptions;

import com.parkingtime.common.enums.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

@Getter
public class LocalAuthenticationException extends AuthenticationException {
    private final ErrorCode code;
    private final HttpStatus httpStatus;

    public LocalAuthenticationException(String message) {
        super(message);
        code = ErrorCode.UNAUTHORIZED;
        this.httpStatus = HttpStatus.UNAUTHORIZED;
    }
}
