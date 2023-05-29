package com.parkingtime.common.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.parkingtime.common.enums.RoleEnum;
import lombok.Builder;

import java.util.List;

@Builder
public record AuthenticationResponse(
        @JsonProperty("token_type")
        String tokenType,
        String token,
        String email,
        @JsonProperty("token_expiration")
        String tokenExpiration,
        List<RoleEnum> roles
) {
}
