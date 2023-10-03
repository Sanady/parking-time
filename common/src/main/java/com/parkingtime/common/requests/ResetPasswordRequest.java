package com.parkingtime.common.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.parkingtime.common.annotations.PasswordValidation;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordRequest {
        @PasswordValidation
        @JsonProperty("new_password")
        @NotBlank(message = "New password cannot be empty, please checkout your input.")
        private String newPassword;

        @PasswordValidation
        @JsonProperty("confirmation_password")
        @NotBlank(message = "Confirmation password cannot be empty, please checkout your input.")
        private String confirmPassword;

        private String token;
}
