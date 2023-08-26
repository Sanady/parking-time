package com.parkingtime.authentication.controllers;


import com.parkingtime.authentication.models.Vehicle;
import com.parkingtime.authentication.services.VehicleService;
import com.parkingtime.common.requests.CreateVehicleRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.parkingtime.common.constants.ApplicationConstants.VEHICLE;
import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@RestController
@RequestMapping(VEHICLE)
@RequiredArgsConstructor
public class VehicleController {
    private final VehicleService vehicleService;

    @PreAuthorize(value = "hasRole('ROLE_USER')")
    @PostMapping("")
    public ResponseEntity<Vehicle> createVehicle(@Validated @RequestBody CreateVehicleRequest request) {
        return ResponseEntity
                .status(CREATED)
                .body(vehicleService.createVehicle(request));
    }

    @PreAuthorize(value = "hasRole('ROLE_USER')")
    @GetMapping("/{vehicleId}")
    public ResponseEntity<Vehicle> getVehicle(@PathVariable Long vehicleId) {
        return ResponseEntity.ok(vehicleService.getVehicle(vehicleId));
    }

    @PreAuthorize(value = "hasRole('ROLE_USER')")
    @GetMapping("/licence-plate/{licencePlate}")
    public ResponseEntity<Vehicle> getVehicleByLicencePlate(@PathVariable String licencePlate) {
        return ResponseEntity.ok(vehicleService.getVehicleByLicencePlate(licencePlate));
    }

    @PreAuthorize(value = "hasRole('ROLE_USER')")
    @DeleteMapping("/{vehicleId}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long vehicleId) {
        vehicleService.deleteVehicle(vehicleId);
        return ResponseEntity
                .accepted()
                .build();
    }

}
