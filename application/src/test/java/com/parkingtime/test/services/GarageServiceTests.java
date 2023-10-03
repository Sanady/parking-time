package com.parkingtime.test.services;

import com.parkingtime.common.enums.GarageStatus;
import com.parkingtime.common.enums.GarageType;
import com.parkingtime.common.requests.CreateGarageRequest;
import com.parkingtime.common.responses.CreateGarageResponse;
import com.parkingtime.models.Garage;
import com.parkingtime.repositories.GarageGeolocationRepository;
import com.parkingtime.repositories.GarageRepository;
import com.parkingtime.services.GarageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GarageServiceTests {
    @Mock
    private GarageRepository garageRepository;

    @Mock
    private GarageGeolocationRepository garageGeolocationRepository;

    @InjectMocks
    private GarageService garageService;

    public static Object[][] testDataProviderAlreadyExists() {
        return new Object[][] {
                {
                        CreateGarageRequest.builder()
                                .name("Garage 1")
                                .total(100)
                                .status(GarageStatus.OPEN)
                                .type(GarageType.PUBLIC)
                                .latitude(1.0)
                                .longitude(1.0)
                                .build(),
                        true,
                        false
                },
                {
                        CreateGarageRequest.builder()
                                .name("Garage 1")
                                .total(100)
                                .status(GarageStatus.OPEN)
                                .type(GarageType.PUBLIC)
                                .latitude(1.0)
                                .longitude(1.0)
                                .build(),
                        false,
                        true
                },
                {
                        CreateGarageRequest.builder()
                                .name("Garage 1")
                                .total(100)
                                .status(GarageStatus.OPEN)
                                .type(GarageType.PUBLIC)
                                .latitude(1.0)
                                .longitude(1.0)
                                .build(),
                        true,
                        true
                }
        };
    }

    public static Object[][] testDataProviderInvalidData() {
        return new Object[][] {
                {
                        CreateGarageRequest.builder()
                                .name("Garage 1")
                                .total(100)
                                .status(GarageStatus.OPEN)
                                .type(null)
                                .latitude(1.0)
                                .longitude(1.0)
                                .build(),
                        "Garage type cannot be empty, please checkout your input."
                },
                {
                        CreateGarageRequest.builder()
                                .name("Garage 1")
                                .total(100)
                                .status(null)
                                .type(GarageType.PUBLIC)
                                .latitude(1.0)
                                .longitude(1.0)
                                .build(),
                        "Garage status cannot be empty, please checkout your input."
                },
                {
                        CreateGarageRequest.builder()
                                .name(null)
                                .total(100)
                                .status(GarageStatus.OPEN)
                                .type(GarageType.PUBLIC)
                                .latitude(1.0)
                                .longitude(1.0)
                                .build(),
                        "Garage name cannot be empty, please checkout your input."
                },
                {
                        CreateGarageRequest.builder()
                                .name("Garage 1")
                                .total(null)
                                .status(GarageStatus.OPEN)
                                .type(GarageType.PUBLIC)
                                .latitude(1.0)
                                .longitude(1.0)
                                .build(),
                        "Total number of parking spots cannot be empty, please checkout your input."
                },
                {
                        CreateGarageRequest.builder()
                                .name("Garage 1")
                                .total(100)
                                .status(GarageStatus.OPEN)
                                .type(GarageType.PUBLIC)
                                .latitude(null)
                                .longitude(1.0)
                                .build(),
                        "Garage latitude cannot be empty, please checkout your input."
                },
                {
                        CreateGarageRequest.builder()
                                .name("Garage 1")
                                .total(100)
                                .status(GarageStatus.OPEN)
                                .type(GarageType.PUBLIC)
                                .latitude(1.0)
                                .longitude(null)
                                .build(),
                        "Garage longitude cannot be empty, please checkout your input."
                },
                {
                        CreateGarageRequest.builder()
                                .name(null)
                                .total(null)
                                .status(GarageStatus.OPEN)
                                .type(GarageType.PUBLIC)
                                .latitude(1.0)
                                .longitude(1.0)
                                .build(),
                        "Garage name cannot be empty, please checkout your input."
                }
        };
    }

    @Test
    @DisplayName("Garage - create garage - success")
    void createGarage_createGarage_shouldReturnSuccess() {
        // Arrange
        CreateGarageRequest request = CreateGarageRequest.builder()
                .name("Garage 1")
                .total(100)
                .status(GarageStatus.OPEN)
                .type(GarageType.PUBLIC)
                .latitude(1.0)
                .longitude(1.0)
                .build();
        when(garageRepository.existsByName("Garage 1")).thenReturn(false);
        when(garageGeolocationRepository.existsByLatitudeAndLongitude(1.0, 1.0)).thenReturn(false);

        // Act
        CreateGarageResponse response = garageService.createGarage(request);

        // Assert
        Assertions.assertAll(() -> {
            Assertions.assertEquals(request.getName(), response.name());
            Assertions.assertEquals(request.getTotal(), response.total());
            Assertions.assertEquals(request.getStatus().name(), response.status());
            Assertions.assertEquals(request.getType().name(), response.type());
            Assertions.assertEquals(request.getLatitude(), response.latitude());
            Assertions.assertEquals(request.getLongitude(), response.longitude());
        });
    }

    @ParameterizedTest(name = "#{index} - Test with Argument={0}, existsByName={1}, existsByLatitudeAndLongitude={2}")
    @MethodSource("testDataProviderAlreadyExists")
    @DisplayName("Garage - create garage - already exists")
    void createGarage_createGarage_shouldReturnAlreadyExists(CreateGarageRequest request,
                                                             boolean existsByName,
                                                             boolean existsByLatitudeAndLongitude) {
        // Arrange
        when(garageRepository.existsByName("Garage 1"))
                .thenReturn(existsByName);
        if(!existsByName) {
            when(garageGeolocationRepository.existsByLatitudeAndLongitude(request.getLatitude(), request.getLongitude()))
                    .thenReturn(existsByLatitudeAndLongitude);
        }

        // Act
        // Assert
        Assertions.assertThrows(Exception.class, () -> garageService.createGarage(request));
    }

    @ParameterizedTest(name = "#{index} - Test with Argument={0}, reason={1}")
    @MethodSource("testDataProviderInvalidData")
    @DisplayName("Garage - create garage - invalid garage type")
    void createGarage_createGarageWithInvalidData_shouldReturnException(CreateGarageRequest request, String reason) {
        // Arrange
        when(garageRepository.existsByName("Garage 1")).thenReturn(false);
        when(garageGeolocationRepository.existsByLatitudeAndLongitude(1.0, 1.0)).thenReturn(false);

        // Act
        // Assert
        Assertions.assertThrows(Exception.class, () -> garageService.createGarage(request));
    }

    @Test
    @DisplayName("Garage - get garage - success")
    void getGarage_getGarage_shouldReturnSuccess() {
        // Arrange
        Garage garage = Garage.builder()
                .id(1L)
                .name("Garage 1")
                .total(100)
                .status(GarageStatus.OPEN.name())
                .type(GarageType.PUBLIC.name())
                .available(100)
                .occupied(0)
                .build();
        when(garageRepository.findById(1L)).thenReturn(Optional.ofNullable(garage));
        // Act
        Garage response = garageService.getGarageById(1L);
        // Assert
        Assertions.assertAll(() -> {
            assert garage != null;
            Assertions.assertEquals(garage.getName(), response.getName());
            Assertions.assertEquals(garage.getTotal(), response.getTotal());
            Assertions.assertEquals(garage.getStatus(), response.getStatus());
            Assertions.assertEquals(garage.getType(), response.getType());
            Assertions.assertEquals(garage.getAvailable(), response.getAvailable());
            Assertions.assertEquals(garage.getOccupied(), response.getOccupied());
        });
    }

    @Test
    @DisplayName("Garage - get garage - not found")
    void getGarage_getGarage_shouldReturnNotFound() {
        // Arrange
        when(garageRepository.findById(1L)).thenReturn(Optional.empty());
        // Act
        // Assert
        Assertions.assertThrows(Exception.class, () -> garageService.getGarageById(1L));
    }

    @Test
    @DisplayName("Garage - get garage - invalid id")
    void getGarage_getGarage_shouldReturnInvalidId() {
        // Arrange
        when(garageRepository.findById(1L)).thenReturn(Optional.empty());
        // Act
        // Assert
        Assertions.assertThrows(Exception.class, () -> garageService.getGarageById(null));
    }

    @Test
    @DisplayName("Garage - delete garage - success")
    void deleteGarage_deleteGarage_shouldReturnSuccess() {
        // Arrange
        Garage garage = Garage.builder()
                .id(1L)
                .name("Garage 1")
                .total(100)
                .status(GarageStatus.OPEN.name())
                .type(GarageType.PUBLIC.name())
                .available(100)
                .occupied(0)
                .build();
        when(garageRepository.existsById(1L)).thenReturn(true);
        // Act
        garageService.deleteGarageById(1L);
        // Assert
        Assertions.assertAll(() -> {
            assert garage != null;
            Assertions.assertEquals("Garage 1", garage.getName());
            Assertions.assertEquals(100, garage.getTotal());
            Assertions.assertEquals(garage.getStatus(), GarageStatus.OPEN.name());
            Assertions.assertEquals(garage.getType(), GarageType.PUBLIC.name());
            Assertions.assertEquals(100, garage.getAvailable());
            Assertions.assertEquals(0, garage.getOccupied());
        });
    }

    @Test
    @DisplayName("Garage - delete garage - not found")
    void deleteGarage_deleteGarage_shouldReturnNotFound() {
        // Arrange
        when(garageRepository.existsById(1L)).thenReturn(false);
        // Act
        // Assert
        Assertions.assertThrows(Exception.class, () -> garageService.deleteGarageById(1L));
    }

    @Test
    @DisplayName("Garage - delete garage - invalid id")
    void deleteGarage_deleteGarage_shouldReturnInvalidId() {
        // Arrange
        when(garageRepository.existsById(1L)).thenReturn(true);
        // Act
        // Assert
        Assertions.assertThrows(Exception.class, () -> garageService.deleteGarageById(null));
    }
}
