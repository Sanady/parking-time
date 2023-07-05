package com.parkingtime.common.requests;

import com.parkingtime.common.constants.ApplicationConstants;
import jakarta.validation.constraints.Email;
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
        private String email;

        private String password;
}
