package com.parkingtime.authentication.services;

import com.parkingtime.authentication.models.User;
import com.parkingtime.authentication.models.Vehicle;
import com.parkingtime.authentication.repositories.UserRepository;
import com.parkingtime.authentication.repositories.VehicleRepository;
import com.parkingtime.common.enums.VehicleTypeEnum;
import com.parkingtime.common.requests.CreateVehicleRequest;
import com.parkingtime.common.responses.CreateVehicleResponse;
import com.parkingtime.common.utilities.Randomizer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTests {
    @Mock
    private UserRepository userRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private VehicleService vehicleService;

    @Test
    @DisplayName("createVehicle - create new vehicle - happy case")
    void createVehicle_createNewVehicle_returnsResponseObject() {
        // Create a user
        User user = User.builder()
                .id(1L)
                .email(Randomizer.alphabeticGenerator(10) + "@gmail.com")
                .password(Randomizer.alphabeticGenerator(10))
                .build();

        // Create a vehicle
        Vehicle vehicle = Vehicle.builder()
                .id(1L)
                .user(user)
                .licencePlate(Randomizer.alphabeticGenerator(8))
                .vehicleType(VehicleTypeEnum.CAR.name())
                .createdAt(LocalDateTime.now())  // Set the createdAt field
                .build();

        // Create a create vehicle request
        CreateVehicleRequest createVehicleRequest = CreateVehicleRequest.builder()
                .userId(user.getId())
                .vehicleType("car")
                .licencePlate(vehicle.getLicencePlate())
                .build();

        // Create a create vehicle response
        CreateVehicleResponse createVehicleResponse = CreateVehicleResponse.builder()
                .licencePlate(vehicle.getLicencePlate())
                .createdAt(vehicle.getCreatedAt())
                .build();

        // Mock the user repository
        given(userRepository.findById(createVehicleRequest.getUserId()))
                .willReturn(Optional.of(user));

        // Mock the vehicle repository
        given(vehicleRepository.save(any(Vehicle.class)))
                .willReturn(vehicle);

        // Call the vehicle service
        Vehicle createdVehicle = vehicleService.createVehicle(createVehicleRequest);

        // Assert the vehicle service response
        Assertions.assertEquals(createVehicleResponse.licencePlate(), createdVehicle.getLicencePlate());
        Assertions.assertEquals(createVehicleResponse.createdAt(), createdVehicle.getCreatedAt());
    }

    @Test
    @DisplayName("createVehicle - user not found - bad case")
    void createVehicle_userNotFound_throwsException() {
        // Create a create vehicle request
        CreateVehicleRequest createVehicleRequest = CreateVehicleRequest.builder()
                .userId(1L)
                .licencePlate(Randomizer.alphabeticGenerator(8))
                .build();

        // Mock the user repository
        given(userRepository.findById(createVehicleRequest.getUserId()))
                .willReturn(Optional.empty());

        // Call the vehicle service
        Assertions.assertThrows(Exception.class, () -> vehicleService.createVehicle(createVehicleRequest));
    }

    @Test
    @DisplayName("createVehicle - vehicle already exists - bad case")
    void createVehicle_vehicleAlreadyExists_throwsException() {
        // Create a user
        User user = User.builder()
                .id(1L)
                .email(Randomizer.alphabeticGenerator(10) + "@gmail.com")
                .password(Randomizer.alphabeticGenerator(10))
                .build();

        // Create a vehicle
        Vehicle vehicle = Vehicle.builder()
                .id(1L)
                .user(user)
                .licencePlate(Randomizer.alphabeticGenerator(8))
                .createdAt(LocalDateTime.now())  // Set the createdAt field
                .build();

        // Create a create vehicle request
        CreateVehicleRequest createVehicleRequest = CreateVehicleRequest.builder()
                .userId(user.getId())
                .licencePlate(vehicle.getLicencePlate())
                .build();

        // Mock the user repository
        given(userRepository.findById(createVehicleRequest.getUserId()))
                .willReturn(Optional.of(user));

        // Mock the vehicle repository
        given(vehicleRepository.existsVehicleByUserAndLicencePlate(user, createVehicleRequest.getLicencePlate()))
                .willReturn(true);

        // Call the vehicle service
        Assertions.assertThrows(Exception.class, () -> vehicleService.createVehicle(createVehicleRequest));
    }

    @Test
    @DisplayName("createVehicle - user already has 5 vehicles - bad case")
    void createVehicle_userAlreadyHasFiveVehicles_throwsException() {
        // Create a user
        User user = User.builder()
                .id(1L)
                .email(Randomizer.alphabeticGenerator(10) + "@gmail.com")
                .password(Randomizer.alphabeticGenerator(10))
                .build();

        // Create a vehicle
        Vehicle vehicle = Vehicle.builder()
                .id(1L)
                .user(user)
                .licencePlate(Randomizer.alphabeticGenerator(8))
                .createdAt(LocalDateTime.now())  // Set the createdAt field
                .build();

        // Create a create vehicle request
        CreateVehicleRequest createVehicleRequest = CreateVehicleRequest.builder()
                .userId(user.getId())
                .licencePlate(vehicle.getLicencePlate())
                .build();

        // Mock the user repository
        given(userRepository.findById(createVehicleRequest.getUserId()))
                .willReturn(Optional.of(user));

        // Mock the vehicle repository
        given(vehicleRepository.existsVehicleByUserAndLicencePlate(user, createVehicleRequest.getLicencePlate()))
                .willReturn(false);

        // Mock the vehicle repository
        given(vehicleRepository.countVehiclesByUser(user))
                .willReturn(5);

        // Call the vehicle service
        Assertions.assertThrows(Exception.class, () -> vehicleService.createVehicle(createVehicleRequest));
    }

    @Test
    @DisplayName("createVehicle - vehicle type not valid - bad case")
    void createVehicle_vehicleTypeNotValid_throwsException() {
        // Create a user
        User user = User.builder()
                .id(1L)
                .email(Randomizer.alphabeticGenerator(10) + "@gmail.com")
                .password(Randomizer.alphabeticGenerator(10))
                .build();

        // Create a vehicle
        Vehicle vehicle = Vehicle.builder()
                .id(1L)
                .user(user)
                .licencePlate(Randomizer.alphabeticGenerator(8))
                .createdAt(LocalDateTime.now())  // Set the createdAt field
                .build();

        // Create a create vehicle request
        CreateVehicleRequest createVehicleRequest = CreateVehicleRequest.builder()
                .userId(user.getId())
                .licencePlate(vehicle.getLicencePlate())
                .vehicleType("invalid")
                .build();

        // Mock the user repository
        given(userRepository.findById(createVehicleRequest.getUserId()))
                .willReturn(Optional.of(user));

        // Mock the vehicle repository
        given(vehicleRepository.existsVehicleByUserAndLicencePlate(user, createVehicleRequest.getLicencePlate()))
                .willReturn(false);

        // Mock the vehicle repository
        given(vehicleRepository.countVehiclesByUser(user))
                .willReturn(4);

        // Call the vehicle service
        Assertions.assertThrows(Exception.class, () -> vehicleService.createVehicle(createVehicleRequest));
    }

    @Test
    @DisplayName("getVehicle - get vehicle by id - happy case")
    void getVehicle_getVehicleById_returnsVehicle() {
        // Create a user
        User user = User.builder()
                .id(1L)
                .email(Randomizer.alphabeticGenerator(10) + "@gmail.com")
                .password(Randomizer.alphabeticGenerator(10))
                .build();

        // Create a vehicle
        Vehicle vehicle = Vehicle.builder()
                .id(1L)
                .user(user)
                .licencePlate(Randomizer.alphabeticGenerator(8))
                .createdAt(LocalDateTime.now())  // Set the createdAt field
                .build();

        // Mock the vehicle repository
        given(vehicleRepository.findById(vehicle.getId()))
                .willReturn(Optional.of(vehicle));

        // Call the vehicle service
        Vehicle foundVehicle = vehicleService.getVehicle(vehicle.getId());

        // Assert the vehicle service response
        Assertions.assertEquals(vehicle.getId(), foundVehicle.getId());
        Assertions.assertEquals(vehicle.getLicencePlate(), foundVehicle.getLicencePlate());
        Assertions.assertEquals(vehicle.getCreatedAt(), foundVehicle.getCreatedAt());
    }

    @Test
    @DisplayName("getVehicle - vehicle not found - bad case")
    void getVehicle_vehicleNotFound_throwsException() {
        // Create a vehicle
        Vehicle vehicle = Vehicle.builder()
                .id(1L)
                .licencePlate(Randomizer.alphabeticGenerator(8))
                .createdAt(LocalDateTime.now())  // Set the createdAt field
                .build();

        // Mock the vehicle repository
        given(vehicleRepository.findById(vehicle.getId()))
                .willReturn(Optional.empty());

        // Call the vehicle service
        Assertions.assertThrows(Exception.class, () -> vehicleService.getVehicle(vehicle.getId()));
    }

    @Test
    @DisplayName("getVehicleByLicencePlate - get vehicle by licence plate - happy case")
    void getVehicleByLicencePlate_getVehicleByLicencePlate_returnsVehicle() {
        // Create a user
        User user = User.builder()
                .id(1L)
                .email(Randomizer.alphabeticGenerator(10) + "@gmail.com")
                .password(Randomizer.alphabeticGenerator(10))
                .build();

        // Create a vehicle
        Vehicle vehicle = Vehicle.builder()
                .id(1L)
                .user(user)
                .licencePlate(Randomizer.alphabeticGenerator(8))
                .createdAt(LocalDateTime.now())  // Set the createdAt field
                .build();

        // Mock the vehicle repository
        given(vehicleRepository.findByLicencePlate(vehicle.getLicencePlate()))
                .willReturn(Optional.of(vehicle));

        // Call the vehicle service
        Vehicle foundVehicle = vehicleService.getVehicleByLicencePlate(vehicle.getLicencePlate());

        // Assert the vehicle service response
        Assertions.assertEquals(vehicle.getId(), foundVehicle.getId());
        Assertions.assertEquals(vehicle.getLicencePlate(), foundVehicle.getLicencePlate());
        Assertions.assertEquals(vehicle.getCreatedAt(), foundVehicle.getCreatedAt());
    }

    @Test
    @DisplayName("getVehicleByLicencePlate - vehicle not found - bad case")
    void getVehicleByLicencePlate_vehicleNotFound_throwsException() {
        // Create a vehicle
        Vehicle vehicle = Vehicle.builder()
                .id(1L)
                .licencePlate(Randomizer.alphabeticGenerator(8))
                .createdAt(LocalDateTime.now())  // Set the createdAt field
                .build();

        // Mock the vehicle repository
        given(vehicleRepository.findByLicencePlate(vehicle.getLicencePlate()))
                .willReturn(Optional.empty());

        // Call the vehicle service
        Assertions.assertThrows(Exception.class, () -> vehicleService.getVehicleByLicencePlate(vehicle.getLicencePlate()));
    }

    @Test
    @DisplayName("deleteVehicle - delete vehicle by id - happy case")
    void deleteVehicle_deleteVehicleById_returnsNothing() {
        // Create a user
        User user = User.builder()
                .id(1L)
                .email(Randomizer.alphabeticGenerator(10) + "@gmail.com")
                .password(Randomizer.alphabeticGenerator(10))
                .build();

        // Create a vehicle
        Vehicle vehicle = Vehicle.builder()
                .id(1L)
                .user(user)
                .licencePlate(Randomizer.alphabeticGenerator(8))
                .createdAt(LocalDateTime.now())  // Set the createdAt field
                .build();

        // Mock the vehicle repository
        given(vehicleRepository.findById(vehicle.getId()))
                .willReturn(Optional.of(vehicle));

        // Call the vehicle service
        Assertions.assertDoesNotThrow(() -> vehicleService.deleteVehicle(vehicle.getId()));
    }

    @Test
    @DisplayName("deleteVehicle - vehicle not found - bad case")
    void deleteVehicle_vehicleNotFound_throwsException() {
        // Create a vehicle
        Vehicle vehicle = Vehicle.builder()
                .id(1L)
                .licencePlate(Randomizer.alphabeticGenerator(8))
                .createdAt(LocalDateTime.now())  // Set the createdAt field
                .build();

        // Mock the vehicle repository
        given(vehicleRepository.findById(vehicle.getId()))
                .willReturn(Optional.empty());

        // Call the vehicle service
        Assertions.assertThrows(Exception.class, () -> vehicleService.deleteVehicle(vehicle.getId()));
    }
}
