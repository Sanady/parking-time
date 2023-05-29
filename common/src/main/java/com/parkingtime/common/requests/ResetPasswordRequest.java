package com.parkingtime.common.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordRequest {
        @JsonProperty("new_password")
        private String newPassword;
        @JsonProperty("confirm_password")
        private String confirmPassword;
        private String token;
}
