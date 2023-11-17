package com.parkingtime.common.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CreateVehicleResponse(
        @JsonProperty("licence_plate")
        String licencePlate,
        @JsonProperty("created_at")
        LocalDateTime createdAt,
        @JsonProperty("user_id")
        Long userId) {
}
