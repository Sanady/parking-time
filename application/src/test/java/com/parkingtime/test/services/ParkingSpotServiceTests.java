package com.parkingtime.test.services;

import com.parkingtime.common.enums.GarageStatus;
import com.parkingtime.common.enums.GarageType;
import com.parkingtime.common.exceptions.NotFoundException;
import com.parkingtime.models.Garage;
import com.parkingtime.models.GarageGeolocation;
import com.parkingtime.models.ParkingSpot;
import com.parkingtime.repositories.GarageRepository;
import com.parkingtime.repositories.ParkingSpotRepository;
import com.parkingtime.services.ParkingSpotService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParkingSpotServiceTests {
    @Mock
    private ParkingSpotRepository parkingSpotRepository;

    @Mock
    private GarageRepository garageRepository;

    @InjectMocks
    private ParkingSpotService parkingSpotService;


    @Test
    @DisplayName("ParkingSpot - createParkingSpots - should create parking spots")
    void createParkingSpots_shouldCreateParkingSpots() {
        // Arrange
        Garage garage = Garage.builder()
                .id(1L)
                .name("Garage 1")
                .total(100)
                .status(GarageStatus.OPEN.name())
                .type(GarageType.PUBLIC.name())
                .garageGeolocation(GarageGeolocation
                        .builder()
                        .id(1L)
                        .latitude(1.0)
                        .longitude(1.0)
                        .build())
                .build();
        when(garageRepository.findById(1L)).thenReturn(java.util.Optional.of(garage));
        when(parkingSpotRepository.findAllByGarageId(1L)).thenReturn(java.util.Optional.empty());
        // Act
        List<ParkingSpot> parkingSpotList = parkingSpotService.createParkingSpots(1L, 100);
        // Assert
        Assertions.assertEquals(100, parkingSpotList.size());
    }

    @Test
    @DisplayName("ParkingSpot - createParkingSpots - should throw IllegalArgumentException when total is greater than garage total")
    void createParkingSpots_shouldThrowIllegalArgumentException_whenTotalIsGreaterThanGarageTotal() {
        // Arrange
        Garage garage = Garage.builder()
                .id(1L)
                .name("Garage 1")
                .total(100)
                .status(GarageStatus.OPEN.name())
                .type(GarageType.PUBLIC.name())
                .garageGeolocation(GarageGeolocation
                        .builder()
                        .id(1L)
                        .latitude(1.0)
                        .longitude(1.0)
                        .build())
                .build();
        when(garageRepository.findById(1L)).thenReturn(java.util.Optional.of(garage));
        // Act
        // Assert
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> parkingSpotService.createParkingSpots(1L, 101));
    }

    @Test
    @DisplayName("ParkingSpot - createParkingSpots - should throw IllegalArgumentException when parking spots already created")
    void createParkingSpots_shouldThrowIllegalArgumentException_whenParkingSpotsAlreadyCreated() {
        // Arrange
        Garage garage = Garage.builder()
                .id(1L)
                .name("Garage 1")
                .total(100)
                .status(GarageStatus.OPEN.name())
                .type(GarageType.PUBLIC.name())
                .garageGeolocation(GarageGeolocation
                        .builder()
                        .id(1L)
                        .latitude(1.0)
                        .longitude(1.0)
                        .build())
                .build();
        when(garageRepository.findById(1L)).thenReturn(java.util.Optional.of(garage));
        when(parkingSpotRepository.findAllByGarageId(1L)).thenReturn(java.util.Optional.of(List.of(ParkingSpot.builder().build())));
        // Act
        // Assert
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> parkingSpotService.createParkingSpots(1L, 100));
    }

    @Test
    @DisplayName("ParkingSpot - createParkingSpots - should throw IllegalArgumentException when total is less than or equal to 0")
    void createParkingSpots_shouldThrowIllegalArgumentException_whenTotalIsLessThanOrEqualTo0() {
        // Arrange
        Garage garage = Garage.builder()
                .id(1L)
                .name("Garage 1")
                .total(100)
                .status(GarageStatus.OPEN.name())
                .type(GarageType.PUBLIC.name())
                .garageGeolocation(GarageGeolocation
                        .builder()
                        .id(1L)
                        .latitude(1.0)
                        .longitude(1.0)
                        .build())
                .build();
        when(garageRepository.findById(1L)).thenReturn(java.util.Optional.of(garage));
        // Act
        // Assert
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> parkingSpotService.createParkingSpots(1L, 0));
    }

    @Test
    @DisplayName("ParkingSpot - getParkingSpots - should return parking spots")
    void getParkingSpots_shouldReturnParkingSpots() {
        // Arrange
        when(parkingSpotRepository.findAllByGarageId(1L)).thenReturn(java.util.Optional.of(List.of(ParkingSpot.builder().build())));
        when(garageRepository.existsById(1L)).thenReturn(true);
        // Act
        List<ParkingSpot> parkingSpotList = parkingSpotService.getParkingSpots(1L);
        // Assert
        Assertions.assertEquals(1, parkingSpotList.size());
    }

    @Test
    @DisplayName("ParkingSpot - getParkingSpots - should throw IllegalArgumentException when garageId is null")
    void getParkingSpots_shouldThrowIllegalArgumentException_whenGarageIdIsNull() {
        // Arrange
        // Act
        // Assert
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> parkingSpotService.getParkingSpots(null));
    }

    @Test
    @DisplayName("ParkingSpot - getParkingSpots - should throw NotFoundException when parking spots not found")
    void getParkingSpots_shouldThrowNotFoundException_whenParkingSpotsNotFound() {
        // Arrange
        when(garageRepository.existsById(1L)).thenReturn(true);
        when(parkingSpotRepository.findAllByGarageId(1L)).thenReturn(java.util.Optional.empty());
        // Act
        // Assert
        Assertions.assertThrows(NotFoundException.class,
                () -> parkingSpotService.getParkingSpots(1L));
    }

    @Test
    @DisplayName("ParkingSpot - getParkingSpot - should return parking spot")
    void getParkingSpot_shouldReturnParkingSpot() {
        // Arrange
        when(parkingSpotRepository.findById(1L)).thenReturn(java.util.Optional.of(ParkingSpot.builder().build()));
        // Act
        ParkingSpot parkingSpot = parkingSpotService.getParkingSpot(1L);
        // Assert
        Assertions.assertNotNull(parkingSpot);
    }

    @Test
    @DisplayName("ParkingSpot - getParkingSpots - should return list of parking spots")
    void getParkingSpots_shouldReturnListOfParkingSpots() {
        // Arrange
        when(parkingSpotRepository.findAllByGarageId(1L)).thenReturn(java.util.Optional.of(List.of(ParkingSpot.builder().build())));
        when(garageRepository.existsById(1L)).thenReturn(true);
        // Act
        List<ParkingSpot> parkingSpotList = parkingSpotService.getParkingSpots(1L);
        // Assert
        Assertions.assertEquals(1, parkingSpotList.size());
    }

    @Test
    @DisplayName("ParkingSpot - getParkingSpots - should throw IllegalArgumentException when id is null")
    void getParkingSpots_shouldThrowIllegalArgumentException_whenIdIsNull() {
        // Arrange
        // Act
        // Assert
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> parkingSpotService.getParkingSpots(null));
    }

    @Test
    @DisplayName("ParkingSpot - updateParkingSpotOccupiedStatus - should return parking spot")
    void updateParkingSpotOccupiedStatus_shouldReturnParkingSpot() {
        // Arrange
        ParkingSpot parkingSpot = ParkingSpot
                .builder()
                .id(1L)
                .occupied(false)
                .garage(Garage
                        .builder()
                        .id(1L)
                        .build())
                .build();
        when(parkingSpotRepository.findById(1L)).thenReturn(java.util.Optional.of(parkingSpot));
        when(parkingSpotRepository.save(parkingSpot)).thenReturn(parkingSpot);
        // Act
        parkingSpot = parkingSpotService.updateParkingSpotOccupiedStatus(1L);
        // Assert
        Assertions.assertNotNull(parkingSpot);
        Assertions.assertTrue(parkingSpot.getOccupied());
    }

    @Test
    @DisplayName("ParkingSpot - updateParkingSpotOccupiedStatus - should throw IllegalArgumentException when id is null")
    void updateParkingSpotOccupiedStatus_shouldThrowIllegalArgumentException_whenIdIsNull() {
        // Arrange
        // Act
        // Assert
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> parkingSpotService.updateParkingSpotOccupiedStatus(null));
    }

    @Test
    @DisplayName("ParkingSpot - updateParkingSpotOccupiedStatus - should throw NotFoundException when parking spot not found")
    void updateParkingSpotOccupiedStatus_shouldThrowNotFoundException_whenParkingSpotNotFound() {
        // Arrange
        when(parkingSpotRepository.findById(1L)).thenReturn(java.util.Optional.empty());
        // Act
        // Assert
        Assertions.assertThrows(NotFoundException.class,
                () -> parkingSpotService.updateParkingSpotOccupiedStatus(1L));
    }

    @Test
    @DisplayName("ParkingSpot - deleteParkingSpot - should throw IllegalArgumentException when id is null")
    void deleteParkingSpot_shouldThrowIllegalArgumentException_whenIdIsNull() {
        // Arrange
        // Act
        // Assert
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> parkingSpotService.deleteParkingSpot(null));
    }

    @Test
    @DisplayName("ParkingSpot - deleteParkingSpot - should throw NotFoundException when parking spot not found")
    void deleteParkingSpot_shouldThrowNotFoundException_whenParkingSpotNotFound() {
        // Arrange
        when(parkingSpotRepository.existsById(1L)).thenReturn(false);
        // Act
        // Assert
        Assertions.assertThrows(NotFoundException.class,
                () -> parkingSpotService.deleteParkingSpot(1L));
    }
}
