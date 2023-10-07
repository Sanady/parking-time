package com.parkingtime.controllers;

import com.parkingtime.models.ParkingSpot;
import com.parkingtime.services.ParkingSpotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.parkingtime.common.constants.ApplicationConstants.GARAGE_BY_ID;
import static com.parkingtime.common.constants.ApplicationConstants.ID;
import static com.parkingtime.common.constants.ApplicationConstants.PARKING_SPOT;
import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@RestController
@RequestMapping(PARKING_SPOT)
@RequiredArgsConstructor
public class ParkingSpotController {
    private final ParkingSpotService parkingSpotService;

    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @PostMapping(GARAGE_BY_ID)
    public ResponseEntity<List<ParkingSpot>> createParkingSpots(@PathVariable Long garageId,
                                                                @RequestParam Integer total) {
        return ResponseEntity
                .status(CREATED)
                .body(parkingSpotService.createParkingSpots(garageId, total));
    }

    @PreAuthorize(value = "hasRole('ROLE_USER')")
    @GetMapping(GARAGE_BY_ID)
    public ResponseEntity<List<ParkingSpot>> getParkingSpots(@PathVariable Long garageId) {
        return ResponseEntity
                .ok()
                .body(parkingSpotService.getParkingSpots(garageId));
    }

    @PreAuthorize(value = "hasRole('ROLE_USER')")
    @GetMapping(ID)
    public ResponseEntity<ParkingSpot> getParkingSpot(@PathVariable Long id) {
        return ResponseEntity
                .ok()
                .body(parkingSpotService.getParkingSpot(id));
    }

    @PatchMapping(ID)
    public ResponseEntity<ParkingSpot> updateParkingSpot(@PathVariable Long id) {
        return ResponseEntity
                .accepted()
                .body(parkingSpotService.updateParkingSpotOccupiedStatus(id));
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @DeleteMapping(ID)
    public ResponseEntity<Void> deleteParkingSpot(@PathVariable Long id) {
        parkingSpotService.deleteParkingSpot(id);
        return ResponseEntity
                .noContent()
                .build();
    }
}
