package com.parkingtime.common.requests;

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
public class CreateCityRequest {
    @NotBlank(message = "City name cannot be empty, please checkout your input.")
    @NotEmpty(message = "City name cannot be empty, please checkout your input.")
    @NotNull(message = "City name cannot be empty, please checkout your input.")
    private String name;
}
