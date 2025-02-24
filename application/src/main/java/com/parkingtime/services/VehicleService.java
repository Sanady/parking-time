package com.parkingtime.services;

import com.parkingtime.common.responses.CreateVehicleResponse;
import com.parkingtime.models.User;
import com.parkingtime.models.Vehicle;
import com.parkingtime.repositories.UserRepository;
import com.parkingtime.repositories.VehicleRepository;
import com.parkingtime.common.enums.VehicleTypeEnum;
import com.parkingtime.common.exceptions.AlreadyExistsException;
import com.parkingtime.common.exceptions.NotFoundException;
import com.parkingtime.common.requests.CreateVehicleRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.parkingtime.common.enums.ErrorMessages.VEHICLE_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class VehicleService {
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;

    public CreateVehicleResponse createVehicle(CreateVehicleRequest createVehicleRequest) {
        String vehicleType;
        User user = userRepository.findById(createVehicleRequest.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (Boolean.TRUE.equals(vehicleRepository.existsVehicleByUserAndLicencePlate(user, createVehicleRequest.getLicencePlate()))) {
            throw new AlreadyExistsException("Vehicle already exists");
        }

        if (vehicleRepository.countVehiclesByUser(user) >= 5) {
            throw new AlreadyExistsException("User already has 5 vehicles");
        }

        switch (createVehicleRequest.getVehicleType()) {
            case "car" -> vehicleType = VehicleTypeEnum.CAR.name();
            case "motorcycle" -> vehicleType = VehicleTypeEnum.MOTORCYCLE.name();
            default -> throw new IllegalArgumentException("Vehicle type not valid");
        }

        Vehicle vehicle = Vehicle.builder()
                .user(user)
                .licencePlate(createVehicleRequest.getLicencePlate())
                .createdAt(LocalDateTime.now())
                .vehicleType(vehicleType)
                .build();
        vehicleRepository.save(vehicle);


        return CreateVehicleResponse
                .builder()
                .userId(user.getId())
                .licencePlate(vehicle.getLicencePlate())
                .createdAt(vehicle.getCreatedAt())
                .build();
    }

    public Vehicle getVehicle(Long vehicleId) {
        return vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new NotFoundException(VEHICLE_NOT_FOUND.getValue()));
    }

    public Vehicle getVehicleByLicencePlate(String licencePlate) {
        return vehicleRepository.findByLicencePlate(licencePlate)
                .orElseThrow(() -> new NotFoundException(VEHICLE_NOT_FOUND.getValue()));
    }

    public void deleteVehicle(Long vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new NotFoundException(VEHICLE_NOT_FOUND.getValue()));
        vehicleRepository.delete(vehicle);
    }
}
