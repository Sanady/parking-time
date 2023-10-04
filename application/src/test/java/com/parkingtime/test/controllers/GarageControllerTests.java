package com.parkingtime.test.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parkingtime.common.enums.GarageStatus;
import com.parkingtime.common.enums.GarageType;
import com.parkingtime.common.exceptions.AlreadyExistsException;
import com.parkingtime.common.requests.CreateGarageRequest;
import com.parkingtime.common.responses.CreateGarageResponse;
import com.parkingtime.controllers.GarageController;
import com.parkingtime.models.Garage;
import com.parkingtime.models.GarageGeolocation;
import com.parkingtime.services.GarageService;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GarageControllerTests {
    @Mock
    private GarageService garageService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        GarageController garageController = new GarageController(garageService);
        mockMvc = MockMvcBuilders.standaloneSetup(garageController).build();
    }

    @Test
    @DisplayName("GarageController - createGarage - should return 201")
    void createGarage() throws Exception {
        CreateGarageRequest request = CreateGarageRequest.builder()
                .name("Garage 1")
                .total(100)
                .status(GarageStatus.OPEN)
                .type(GarageType.PUBLIC)
                .latitude(1.0)
                .longitude(1.0)
                .build();
        CreateGarageResponse response = CreateGarageResponse.builder()
                .name("Garage 1")
                .total(100)
                .status(GarageStatus.OPEN.name())
                .type(GarageType.PUBLIC.name())
                .latitude(1.0)
                .longitude(1.0)
                .build();
        when(garageService.createGarage(request)).thenReturn(response);
        mockMvc.perform(post("/api/v1/garage")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Garage 1"))
                .andExpect(jsonPath("$.total").value(100))
                .andExpect(jsonPath("$.status").value("OPEN"))
                .andExpect(jsonPath("$.type").value("PUBLIC"))
                .andExpect(jsonPath("$.latitude").value(1.0))
                .andExpect(jsonPath("$.longitude").value(1.0));
    }

    @Test
    @DisplayName("GarageController - createGarage - should return 400")
    void createGarage_shouldReturn400() throws Exception {
        CreateGarageRequest request = CreateGarageRequest.builder()
                .name("Garage 1")
                .total(100)
                .status(GarageStatus.OPEN)
                .type(GarageType.PUBLIC)
                .latitude(null)
                .longitude(1.0)
                .build();
        mockMvc.perform(post("/api/v1/garage")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GarageController - createGarage - should return 409")
    void createGarage_shouldReturn409() {
        CreateGarageRequest request = CreateGarageRequest.builder()
                .name("Garage 1")
                .total(100)
                .status(GarageStatus.OPEN)
                .type(GarageType.PUBLIC)
                .latitude(1.0)
                .longitude(1.0)
                .build();
        when(garageService.createGarage(request))
                .thenThrow(new AlreadyExistsException("Garage with name " + request.getName() + " already exists"));
        ServletException existsException = Assertions.assertThrows(ServletException.class,
                () -> mockMvc.perform(post("/api/v1/garage")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(new ObjectMapper().writeValueAsString(request)))
                                        .andExpect(status().isConflict()));
        Assertions.assertTrue(existsException.getMessage()
                        .contains("Garage with name " + request.getName() + " already exists"));
    }

    @Test
    @DisplayName("GarageController - getGarageById - should return 200")
    void getGarageById() throws Exception {
        long garageId = 1L;
        Garage response = Garage.builder()
                .id(garageId)
                .name("Garage 1")
                .total(100)
                .status(GarageStatus.OPEN.name())
                .type(GarageType.PUBLIC.name())
                .garageGeolocation(GarageGeolocation
                        .builder()
                        .id(garageId)
                        .latitude(1.0)
                        .longitude(1.0)
                        .build())
                .build();
        when(garageService.getGarageById(garageId)).thenReturn(response);
        mockMvc.perform(get("/api/v1/garage/{id}", garageId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(response)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Garage 1"))
                .andExpect(jsonPath("$.total").value(100))
                .andExpect(jsonPath("$.status").value("OPEN"))
                .andExpect(jsonPath("$.type").value("PUBLIC"))
                .andExpect(jsonPath("$.garageGeolocation.latitude").value(1.0))
                .andExpect(jsonPath("$.garageGeolocation.longitude").value(1.0));
    }

    @Test
    @DisplayName("GarageController - getGarageById - should return 404")
    void getGarageById_shouldReturn404() {
        long garageId = 1L;
        when(garageService.getGarageById(garageId))
                .thenThrow(new IllegalArgumentException("Garage with id " + garageId + " not found"));
        ServletException existsException = Assertions.assertThrows(ServletException.class,
                () -> mockMvc.perform(get("/api/v1/garage/{id}", garageId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn());
        Assertions.assertTrue(existsException.getMessage()
                .contains("Garage with id " + garageId + " not found"));
    }

    @Test
    @DisplayName("GarageController - deleteGarageById - should return 204")
    void deleteGarageById() throws Exception {
        long garageId = 1L;
        mockMvc.perform(delete("/api/v1/garage/{id}", garageId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
