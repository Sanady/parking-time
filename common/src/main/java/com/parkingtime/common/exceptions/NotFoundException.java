package com.parkingtime.common.exceptions;

import com.parkingtime.common.enums.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class NotFoundException extends RuntimeException{
    private final ErrorCode code;
    private final HttpStatus httpStatus;

    public NotFoundException(String message) {
        super(message);
        code = ErrorCode.NOT_FOUND;
        this.httpStatus = HttpStatus.NOT_FOUND;
    }
}
