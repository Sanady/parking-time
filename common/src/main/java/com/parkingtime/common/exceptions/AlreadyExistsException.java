package com.parkingtime.common.exceptions;

import com.parkingtime.common.enums.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class AlreadyExistsException extends RuntimeException {
    private final ErrorCode code;
    private final HttpStatus httpStatus;

    public AlreadyExistsException(String message) {
        super(message);
        code = ErrorCode.ALREADY_EXISTS;
        this.httpStatus = HttpStatus.CONFLICT;
    }
}
