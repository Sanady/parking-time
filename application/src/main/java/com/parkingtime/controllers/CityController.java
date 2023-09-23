package com.parkingtime.controllers;

import com.parkingtime.common.requests.CreateCityRequest;
import com.parkingtime.common.responses.CreateCityResponse;
import com.parkingtime.models.City;
import com.parkingtime.services.CityService;
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

import static com.parkingtime.common.constants.ApplicationConstants.CITY;

@Slf4j
@RestController
@RequestMapping(CITY)
@RequiredArgsConstructor
public class CityController {
    private final CityService cityService;

    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @PostMapping("")
    public ResponseEntity<CreateCityResponse> createCity(@Validated @RequestBody CreateCityRequest request) {
        return ResponseEntity
                .accepted()
                .body(cityService.createCity(request));
    }

    @PreAuthorize(value = "hasRole('ROLE_USER')")
    @GetMapping("/{name}")
    public ResponseEntity<City> getCityByName(@PathVariable String name) {
        return ResponseEntity
                .accepted()
                .body(cityService.getCityByName(name));
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{name}")
    public ResponseEntity<Void> deleteCityByName(@PathVariable String name) {
        cityService.deleteCityByName(name);
        return ResponseEntity
                .noContent()
                .build();
    }
}
