package com.parkingtime.common.responses;

import com.parkingtime.common.enums.ErrorCode;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponseError {
    @NotNull
    private String code;
    @NotNull
    private String title;

    private String detail;

    private String status;

    public ErrorResponseError(ErrorCode errorCode, String message) {
        this.code = errorCode.getCode();
        this.title = errorCode.getTitle();
        this.detail = message;
    }
}
