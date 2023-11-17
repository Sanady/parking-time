package com.parkingtime.controllers;

import com.parkingtime.common.requests.CreateVehicleRequest;
import com.parkingtime.common.responses.CreateVehicleResponse;
import com.parkingtime.common.responses.ErrorResponse;
import com.parkingtime.models.Vehicle;
import com.parkingtime.services.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.parkingtime.common.constants.ApplicationConstants.VEHICLE;
import static com.parkingtime.common.constants.ApplicationConstants.VEHICLE_BY_ID;
import static com.parkingtime.common.constants.ApplicationConstants.VEHICLE_BY_LICENCE_PLATE_PARAM;
import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@RestController
@RequestMapping(VEHICLE)
@RequiredArgsConstructor
public class VehicleController {
    private final VehicleService vehicleService;

    @Operation(operationId = "createVehicle",
            summary = "Create a new vehicle",
            responses = {
                @ApiResponse(responseCode = "202", description = "Vehicle created successfully",
                        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                schema = @Schema(implementation = CreateVehicleResponse.class))),
                @ApiResponse(responseCode = "400", description = "Invalid request body",
                        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                schema = @Schema(implementation = ErrorResponse.class))),
                @ApiResponse(responseCode = "401", description = "Unauthorized",
                        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                schema = @Schema(implementation = ErrorResponse.class))),
                @ApiResponse(responseCode = "404", description = "Not found",
                        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                schema = @Schema(implementation = ErrorResponse.class))),
                @ApiResponse(responseCode = "409", description = "Conflict",
                        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                schema = @Schema(implementation = ErrorResponse.class))),
                @ApiResponse(responseCode = "500", description = "Internal server error",
                        content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PreAuthorize(value = "hasRole('ROLE_USER')")
    @PostMapping("")
    public ResponseEntity<CreateVehicleResponse> createVehicle(@Validated @RequestBody CreateVehicleRequest request) {
        return ResponseEntity
                .status(CREATED)
                .body(vehicleService.createVehicle(request));
    }

    @Operation(operationId = "createVehicle",
            summary = "Create a new vehicle",
            responses = {
                    @ApiResponse(responseCode = "202", description = "Vehicle created successfully",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = CreateVehicleResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request body",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Not found",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "409", description = "Conflict",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    @PreAuthorize(value = "hasRole('ROLE_USER')")
    @GetMapping(VEHICLE_BY_ID)
    public ResponseEntity<Vehicle> getVehicle(@PathVariable Long vehicleId) {
        return ResponseEntity.ok(vehicleService.getVehicle(vehicleId));
    }

    @PreAuthorize(value = "hasRole('ROLE_USER')")
    @GetMapping(VEHICLE_BY_LICENCE_PLATE_PARAM)
    public ResponseEntity<Vehicle> getVehicleByLicencePlate(@PathVariable String licencePlate) {
        return ResponseEntity.ok(vehicleService.getVehicleByLicencePlate(licencePlate));
    }

    @PreAuthorize(value = "hasRole('ROLE_USER')")
    @DeleteMapping(VEHICLE_BY_ID)
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long vehicleId) {
        vehicleService.deleteVehicle(vehicleId);
        return ResponseEntity
                .accepted()
                .build();
    }

}
