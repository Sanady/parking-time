package com.parkingtime.common.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@JsonPropertyOrder({
        "id",
        "firstname",
        "lastname",
        "email",
        "verified",
        "verified_at",
        "created_at",
        "updated_at"
})
public record GetUserResponse(
        Long id,
        String firstname,
        String lastname,
        String email,
        @JsonProperty("created_at")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss")
        LocalDateTime createdAt,
        @JsonProperty("updated_at")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss")
        LocalDateTime updatedAt,
        Boolean verified,
        @JsonProperty("verified_at")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss")
        LocalDateTime verifiedAt
        ) {
}
