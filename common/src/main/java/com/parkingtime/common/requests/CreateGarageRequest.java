package com.parkingtime.common.requests;

import com.parkingtime.common.enums.GarageStatus;
import com.parkingtime.common.enums.GarageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateGarageRequest {
    @NotBlank(message = "Garage name cannot be empty, please checkout your input.")
    private String name;

    @NotNull(message = "Total number of parking spots cannot be empty, please checkout your input.")
    private Integer total;

    @NotNull(message = "Garage status cannot be empty, please checkout your input.")
    private GarageStatus status;

    @NotNull(message = "Garage type cannot be empty, please checkout your input.")
    private GarageType type;

    @NotNull(message = "Garage latitude cannot be empty, please checkout your input.")
    private Double latitude;

    @NotNull(message = "Garage longitude cannot be empty, please checkout your input.")
    private Double longitude;
}
