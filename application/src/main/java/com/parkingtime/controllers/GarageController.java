package com.parkingtime.controllers;

import com.parkingtime.common.requests.CreateGarageRequest;
import com.parkingtime.common.responses.CreateGarageResponse;
import com.parkingtime.models.Garage;
import com.parkingtime.services.GarageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import static com.parkingtime.common.constants.ApplicationConstants.GARAGE;
import static com.parkingtime.common.constants.ApplicationConstants.ID;
import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@RestController
@RequestMapping(GARAGE)
@RequiredArgsConstructor
public class GarageController {
    private final GarageService garageService;

    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<CreateGarageResponse> createGarage(
            @Validated @RequestBody CreateGarageRequest request) {
        return ResponseEntity
                .status(CREATED)
                .body(garageService.createGarage(request));
    }

    @PreAuthorize(value = "hasRole('ROLE_USER')")
    @GetMapping(ID)
    public ResponseEntity<Garage> getGarage(@PathVariable Long id) {
        return ResponseEntity
                .ok()
                .body(garageService.getGarageById(id));
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @DeleteMapping(ID)
    public ResponseEntity<Void> deleteGarage(@PathVariable Long id) {
        garageService.deleteGarageById(id);
        return ResponseEntity
                .noContent()
                .build();
    }
}
