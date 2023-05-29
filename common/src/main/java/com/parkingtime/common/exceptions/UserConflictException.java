package com.parkingtime.common.exceptions;

import com.parkingtime.common.enums.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UserConflictException extends RuntimeException {
    private final ErrorCode code;
    private final HttpStatus httpStatus;

    public UserConflictException(String message) {
        super(message);
        code = ErrorCode.USER_CONFLICT_EXCEPTION;
        this.httpStatus = HttpStatus.CONFLICT;
    }
}
