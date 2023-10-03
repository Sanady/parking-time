package com.parkingtime.services;

import com.parkingtime.common.enums.GarageStatus;
import com.parkingtime.common.enums.GarageType;
import com.parkingtime.common.exceptions.AlreadyExistsException;
import com.parkingtime.common.requests.CreateGarageRequest;
import com.parkingtime.common.responses.CreateGarageResponse;
import com.parkingtime.models.Garage;
import com.parkingtime.models.GarageGeolocation;
import com.parkingtime.repositories.GarageGeolocationRepository;
import com.parkingtime.repositories.GarageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GarageService {
    private final GarageRepository garageRepository;
    private final GarageGeolocationRepository garageGeolocationRepository;

    public CreateGarageResponse createGarage(CreateGarageRequest request) {
        if(Boolean.TRUE.equals(garageRepository.existsByName(request.getName()))) {
            throw new AlreadyExistsException("Garage with name " + request.getName() + " already exists");
        }

        if(Boolean.TRUE.equals(garageGeolocationRepository.existsByLatitudeAndLongitude(request.getLatitude(),
                                                                  request.getLongitude()))) {
            throw new AlreadyExistsException("Garage with latitude " +
                    request.getLatitude() +
                    " and longitude " +
                    request.getLongitude() +
                    " already exists");
        }

        if(request.getTotal() == null || request.getTotal() <= 0) {
            throw new IllegalArgumentException("Total must be greater than 0");
        }

        GarageType garageType;
        GarageStatus garageStatus;

        garageType = switch (request.getType()) {
            case PRIVATE -> GarageType.PRIVATE;
            case PUBLIC -> GarageType.PUBLIC;
            default -> throw new IllegalArgumentException("Unexpected value from enum: " + request.getType());
        };

        garageStatus = switch (request.getStatus()) {
            case OPEN -> GarageStatus.OPEN;
            case CLOSED -> GarageStatus.CLOSED;
            default -> throw new IllegalArgumentException("Unexpected value from enum: " + request.getStatus());
        };

        Garage garage = Garage.builder()
                .name(request.getName())
                .total(request.getTotal())
                .status(garageStatus.name())
                .type(garageType.name())
                .available(request.getTotal())
                .occupied(0)
                .garageGeolocation(GarageGeolocation.builder()
                        .latitude(request.getLatitude())
                        .longitude(request.getLongitude())
                        .build())
                .build();

        garageRepository.save(garage);
        return CreateGarageResponse.builder()
                .name(garage.getName())
                .total(garage.getTotal())
                .status(garage.getStatus())
                .type(garage.getType())
                .latitude(garage.getGarageGeolocation().getLatitude())
                .longitude(garage.getGarageGeolocation().getLongitude())
                .build();
    }

    public Garage getGarageById(Long id) {
        return garageRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Garage with id " + id + " not found"));
    }

    public void deleteGarageById(Long id) {
        if(Boolean.FALSE.equals(garageRepository.existsById(id))) {
            throw new IllegalArgumentException("Garage with id " + id + " not found");
        }
        garageRepository.deleteById(id);
    }
}
