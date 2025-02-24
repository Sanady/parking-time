package com.parkingtime.common.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeUserPasswordRequest {
    @JsonProperty("old_password")
    @NotBlank(message = "Old password cannot be empty, please checkout your input.")
    private String oldPassword;

    @JsonProperty("new_password")
    @NotBlank(message = "New password cannot be empty, please checkout your input.")
    private String newPassword;

    @JsonProperty("confirmation_password")
    @NotBlank(message = "Confirmation password cannot be empty, please checkout your input.")
    private String confirmPassword;
}
