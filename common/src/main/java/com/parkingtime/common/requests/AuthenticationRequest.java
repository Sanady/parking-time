package com.parkingtime.common.requests;

import com.parkingtime.common.constants.ApplicationConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest {
        @Email(message = "Email has to be in correct format.", regexp = ApplicationConstants.EMAIL_REGEX)
        @NotBlank(message = "Email cannot be empty, please checkout your input.")
        @NotEmpty(message = "Email cannot be empty, please checkout your input.")
        @NotNull(message = "Email cannot be empty, please checkout your input.")
        private String email;
        @NotBlank(message = "Password cannot be empty, please checkout your input.")
        @NotEmpty(message = "Password cannot be empty, please checkout your input.")
        @NotNull(message = "Password cannot be empty, please checkout your input.")
        private String password;
}
