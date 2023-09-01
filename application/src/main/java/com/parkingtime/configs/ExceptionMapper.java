package com.parkingtime.configs;

import com.parkingtime.common.enums.ErrorCode;
import com.parkingtime.common.exceptions.AlreadyExistsException;
import com.parkingtime.common.exceptions.CoverUpMessageException;
import com.parkingtime.common.exceptions.NotFoundException;
import com.parkingtime.common.exceptions.UserConflictException;
import com.parkingtime.common.responses.ErrorResponse;
import com.parkingtime.common.responses.ErrorResponseError;
import com.parkingtime.common.responses.MessageResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class ExceptionMapper {
    @ExceptionHandler(value = { HttpMessageNotReadableException.class })
    public ResponseEntity<ErrorResponse> hande(HttpServletRequest request, HttpMessageNotReadableException ex) {
        return processInvalidRequest(request, ex);
    }

    @ExceptionHandler(value = { MissingServletRequestParameterException.class })
    public ResponseEntity<ErrorResponse> hande(HttpServletRequest request, MissingServletRequestParameterException ex) {
        return processInvalidRequest(request, ex);
    }

    @ExceptionHandler(value = { MethodArgumentNotValidException.class })
    public ResponseEntity<ErrorResponse> hande(HttpServletRequest request, MethodArgumentNotValidException ex) {
        return processInvalidRequest(request, ex);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(HttpServletRequest request, IllegalArgumentException ex) {
        log.warn("{}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(createErrorResponse(request, ex, ErrorCode.ILLEGAL_ARGUMENT));
    }

    @ExceptionHandler(value = { UserConflictException.class })
    @ResponseStatus(code = HttpStatus.CONFLICT)
    public ResponseEntity<ErrorResponse> handleUserConflictException(HttpServletRequest request,
                                                                     UserConflictException ex) {
        log.warn("{}", ex.getMessage());
        return ResponseEntity
                .status(ex.getHttpStatus())
                .body(createErrorResponse(request, ex, ex.getCode()));
    }

    @ExceptionHandler(value = {NotFoundException.class})
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleNotFoundException(HttpServletRequest request, NotFoundException ex) {
        log.warn("{}", ex.getMessage());
        return ResponseEntity
                .status(ex.getHttpStatus())
                .body(createErrorResponse(request, ex, ex.getCode()));
    }

    @ExceptionHandler(value = {AlreadyExistsException.class})
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleAlreadyExistsException(HttpServletRequest request, AlreadyExistsException ex) {
        log.warn("{}", ex.getMessage());
        return ResponseEntity
                .status(ex.getHttpStatus())
                .body(createErrorResponse(request, ex, ex.getCode()));
    }

    @ExceptionHandler(value = {AuthenticationException.class})
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(HttpServletRequest request, AuthenticationException ex) {
        log.warn("{}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(createErrorResponse(request, ex, ErrorCode.UNAUTHORIZED));
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(HttpServletRequest request, ConstraintViolationException ex) {
        log.warn("{}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(createErrorResponse(request, ex, ErrorCode.CONSTRAINT_VIOLATION));
    }

    @ExceptionHandler(value = {ExpiredJwtException.class})
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponse> handleExpiredJwtException(HttpServletRequest request, ExpiredJwtException ex) {
        log.warn("{}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(createErrorResponse(request, ex, ErrorCode.UNAUTHORIZED));
    }

    @ExceptionHandler(value = {UnsupportedJwtException.class})
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponse> handleUnsupportedJwtException(HttpServletRequest request, UnsupportedJwtException ex) {
        log.warn("{}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(createErrorResponse(request, ex, ErrorCode.UNAUTHORIZED));
    }

    @ExceptionHandler(value = {MalformedJwtException.class})
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponse> handleMalformedJwtException(HttpServletRequest request, MalformedJwtException ex) {
        log.warn("{}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(createErrorResponse(request, ex, ErrorCode.UNAUTHORIZED));
    }

    @ExceptionHandler(value = {SignatureException.class})
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponse> handleSignatureException(HttpServletRequest request, SignatureException ex) {
        log.warn("{}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(createErrorResponse(request, ex, ErrorCode.UNAUTHORIZED));
    }

    @ExceptionHandler(value = {CoverUpMessageException.class})
    @ResponseStatus(code = HttpStatus.ACCEPTED)
    public ResponseEntity<MessageResponse> handleCoverUpMessageException(CoverUpMessageException ex) {
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(new MessageResponse(ex.getMessage()));
    }

    private ErrorResponse createErrorResponse(HttpServletRequest request, Exception e, ErrorCode code) {
        return ErrorResponse.builder()
                .errors(List.of(
                        new ErrorResponseError(code, e.getMessage())
                ))
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();
    }

    private ResponseEntity<ErrorResponse> processInvalidRequest(HttpServletRequest request, Exception e) {
        log.warn("Invalid request error: {}", e.getMessage());
        return ResponseEntity
                .badRequest()
                .body(createErrorResponse(request, e, ErrorCode.INVALID_REQUEST_DATA));
    }
}
