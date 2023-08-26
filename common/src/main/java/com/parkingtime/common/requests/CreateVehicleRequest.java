package com.parkingtime.common.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class CreateVehicleRequest {

    @JsonProperty("user_id")
    @NotNull(message = "User id cannot be empty, please checkout your input.")
    private Long userId;

    @JsonProperty("licence_plate")
    @NotBlank(message = "Licence plate cannot be empty, please checkout your input.")
    @NotEmpty(message = "Licence plate cannot be empty, please checkout your input.")
    @NotNull(message = "Licence plate cannot be empty, please checkout your input.")
    private String licencePlate;

    @JsonProperty("vehicle_type")
    @NotBlank(message = "Vehicle type cannot be empty, please checkout your input.")
    @NotEmpty(message = "Vehicle type cannot be empty, please checkout your input.")
    @NotNull(message = "Vehicle type cannot be empty, please checkout your input.")
    private String vehicleType;
}
