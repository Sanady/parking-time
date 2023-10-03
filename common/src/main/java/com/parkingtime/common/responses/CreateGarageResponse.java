package com.parkingtime.common.responses;

import lombok.Builder;

@Builder
public record CreateGarageResponse(
        String name,
        Integer total,
        String status,
        String type,
        Double latitude,
        Double longitude
) {
}
