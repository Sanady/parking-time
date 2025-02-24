package com.parkingtime.common.requests;

import com.parkingtime.common.annotations.PasswordValidation;
import com.parkingtime.common.constants.ApplicationConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
        @Size(max = 32)
        @Pattern(regexp = ApplicationConstants.ONLY_CHARACTERS)
        @NotBlank(message = "Firstname cannot be empty, please checkout your input.")
        private String firstname;

        @Size(max = 32)
        @Pattern(regexp = ApplicationConstants.ONLY_CHARACTERS)
        @NotBlank(message = "Lastname cannot be empty, please checkout your input.")
        private String lastname;

        @Email(message = "Email has to be in correct format.", regexp = ApplicationConstants.EMAIL_REGEX)
        @NotBlank(message = "Email cannot be empty, please checkout your input.")
        private String email;

        @PasswordValidation
        private String password;

        private Set<String> roles;
}
