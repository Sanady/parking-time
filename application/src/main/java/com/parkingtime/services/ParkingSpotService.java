package com.parkingtime.services;

import com.parkingtime.common.exceptions.NotFoundException;
import com.parkingtime.models.Garage;
import com.parkingtime.models.ParkingSpot;
import com.parkingtime.repositories.GarageRepository;
import com.parkingtime.repositories.ParkingSpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.parkingtime.common.enums.ErrorMessages.GARAGE_NOT_FOUND;
import static com.parkingtime.common.enums.ErrorMessages.PARKING_SPOT_ALREADY_CREATED;
import static com.parkingtime.common.enums.ErrorMessages.PARKING_SPOT_CANNOT_BE_NULL;
import static com.parkingtime.common.enums.ErrorMessages.PARKING_SPOT_GREATER_THEN_ZERO;
import static com.parkingtime.common.enums.ErrorMessages.PARKING_SPOT_NOT_FOUND;
import static com.parkingtime.common.enums.ErrorMessages.PARKING_SPOT_TOTAL_CANNOT_BE_NULL;

@Service
@RequiredArgsConstructor
public class ParkingSpotService {
    private final ParkingSpotRepository parkingSpotRepository;
    private final GarageRepository garageRepository;

    public List<ParkingSpot> createParkingSpots(Long garageId, Integer total) {
        ArrayList<ParkingSpot> parkingSpotsList = new ArrayList<>();
        Garage garage = garageRepository.findById(garageId)
                .orElseThrow(() -> new NotFoundException(GARAGE_NOT_FOUND.getValue()));

        if(total > garage.getTotal()) {
            throw new IllegalArgumentException(PARKING_SPOT_GREATER_THEN_ZERO.getValue());
        }

        if (parkingSpotRepository.findAllByGarageId(garageId).isPresent()) {
            throw new IllegalArgumentException(PARKING_SPOT_ALREADY_CREATED.getValue());
        }

        if(total <= 0) {
            throw new IllegalArgumentException(PARKING_SPOT_TOTAL_CANNOT_BE_NULL.getValue());
        }

        for (int i = 0; i < total; i++) {
            ParkingSpot parkingSpot = ParkingSpot.builder()
                    .occupied(false)
                    .garage(garage)
                    .build();
            parkingSpotsList.add(parkingSpot);
        }
        parkingSpotRepository.saveAll(parkingSpotsList);
        return parkingSpotsList;
    }

    public List<ParkingSpot> getParkingSpots(Long garageId) {
        if(garageId == null) {
            throw new IllegalArgumentException(GARAGE_NOT_FOUND.getValue());
        }

        if(!garageRepository.existsById(garageId)) {
            throw new NotFoundException(GARAGE_NOT_FOUND.getValue());
        }

        return parkingSpotRepository.findAllByGarageId(garageId)
                .orElseThrow(() -> new NotFoundException(PARKING_SPOT_NOT_FOUND.getValue()));
    }

    public ParkingSpot getParkingSpot(Long id) {
        if(id == null) {
            throw new IllegalArgumentException(PARKING_SPOT_CANNOT_BE_NULL.getValue());
        }

        return parkingSpotRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(PARKING_SPOT_NOT_FOUND.getValue()));
    }

    public ParkingSpot updateParkingSpotOccupiedStatus(Long id) {
        if(id == null) {
            throw new IllegalArgumentException(PARKING_SPOT_CANNOT_BE_NULL.getValue());
        }

        ParkingSpot parkingSpotToUpdate = parkingSpotRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(PARKING_SPOT_NOT_FOUND.getValue()));

        parkingSpotToUpdate.setOccupied(!parkingSpotToUpdate.getOccupied());
        return parkingSpotRepository.save(parkingSpotToUpdate);
    }

    public void deleteParkingSpot(Long id) {
        if(id == null) {
            throw new IllegalArgumentException(PARKING_SPOT_CANNOT_BE_NULL.getValue());
        }

        if(!parkingSpotRepository.existsById(id)) {
            throw new NotFoundException(PARKING_SPOT_NOT_FOUND.getValue());
        }
        parkingSpotRepository.deleteById(id);
    }
}
