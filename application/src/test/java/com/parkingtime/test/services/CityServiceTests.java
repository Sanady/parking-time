package com.parkingtime.test.services;

import com.parkingtime.common.requests.CreateCityRequest;
import com.parkingtime.common.utilities.Randomizer;
import com.parkingtime.models.City;
import com.parkingtime.repositories.CityRepository;
import com.parkingtime.services.CityService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CityServiceTests {
    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private CityService cityService;

    @Test
    @DisplayName("City - create city with invalid name - fail")
    void createCity_createNewCityWithInvalidName_shouldReturnIllegalArgumentException() {
        // Arrange
        CreateCityRequest request = new CreateCityRequest(Randomizer.alphaNumericGenerator(15));
        // Act
        // Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> cityService.createCity(request));
    }

    @Test
    @DisplayName("City - create city with existing name - faile")
    void createCity_createNewCityWithExistingName_shouldReturnIllegalArgumentException() {
        // Arrange
        CreateCityRequest request = new CreateCityRequest("TestCity");
        when(cityRepository.existsByName(request.getName())).thenReturn(true);
        // Act
        // Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> cityService.createCity(request));
    }

    @Test
    @DisplayName("City - create city with valid name - success")
    void createCity_createNewCityWithValidName_shouldReturnCreateCityResponse() {
        // Arrange
        CreateCityRequest request = new CreateCityRequest("TestCity");
        when(cityRepository.existsByName(request.getName())).thenReturn(false);
        // Act
        // Assert
        Assertions.assertDoesNotThrow(() -> cityService.createCity(request));
    }

    @Test
    @DisplayName("City - get city by name - success")
    void getCityByName_getCityByName_shouldReturnCity() {
        // Arrange
        String name = "TestCity";
        City city = new City(name);
        when(cityRepository.findByName(name)).thenReturn(Optional.of(city));
        // Act
        // Assert
        Assertions.assertDoesNotThrow(() -> cityService.getCityByName(name));
    }

    @Test
    @DisplayName("City - get city by name - failure")
    void getCityByName_getCityByName_shouldReturnIllegalArgumentException() {
        // Arrange
        String name = "TestCity";
        when(cityRepository.findByName(name)).thenReturn(Optional.empty());
        // Act
        // Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> cityService.getCityByName(name));
    }

    @Test
    @DisplayName("City - delete city by name - success")
    void deleteCityByName_deleteCityByName_shouldReturnVoid() {
        // Arrange
        String name = "TestCity";
        City city = new City(name);
        when(cityRepository.findByName(name)).thenReturn(java.util.Optional.of(city));
        // Act
        // Assert
        Assertions.assertDoesNotThrow(() -> cityService.deleteCityByName(name));
    }

    @Test
    @DisplayName("City - delete city by name - failure")
    void deleteCityByName_deleteCityByName_shouldReturnIllegalArgumentException() {
        // Arrange
        String name = "TestCity";
        when(cityRepository.findByName(name)).thenReturn(java.util.Optional.empty());
        // Act
        // Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> cityService.deleteCityByName(name));
    }
}
