package com.parkingtime.common.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ErrorResponse(@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss")
                            LocalDateTime timestamp,
                            String path,
                            List<ErrorResponseError> errors) {
}
