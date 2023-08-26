package com.parkingtime.authentication.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parkingtime.authentication.models.Role;
import com.parkingtime.authentication.models.User;
import com.parkingtime.authentication.models.Vehicle;
import com.parkingtime.authentication.services.VehicleService;
import com.parkingtime.common.enums.RoleEnum;
import com.parkingtime.common.enums.VehicleTypeEnum;
import com.parkingtime.common.requests.CreateVehicleRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class VehicleControllerTests {
    @Mock
    private VehicleService vehicleService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        VehicleController vehicleController = new VehicleController(vehicleService);
        mockMvc = MockMvcBuilders.standaloneSetup(vehicleController).build();
    }

    @Test
    @DisplayName("VehicleController - create vehicle - should return 201 CREATED")
    void createVehicle() throws Exception {
        long userId = 1L;
        String licencePlate = "ABCD123";

        CreateVehicleRequest request = CreateVehicleRequest
                .builder()
                .userId(userId)
                .licencePlate(licencePlate)
                .vehicleType("car")
                .build();

        Vehicle createdVehicle = Vehicle.builder()
                .licencePlate(licencePlate)
                .user(User
                        .builder()
                        .id(userId)
                        .roles(Set.of(new Role(RoleEnum.ROLE_USER)))
                        .build())
                .vehicleType(VehicleTypeEnum.CAR.name())
                .build();

        given(vehicleService.createVehicle(any(CreateVehicleRequest.class)))
                .willReturn(createdVehicle);

        mockMvc.perform(post("/api/v1/vehicle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.licencePlate").value(createdVehicle.getLicencePlate()));
    }

    @Test
    @DisplayName("VehicleController - get vehicle - should return 200 OK")
    void getVehicle() throws Exception {
        long vehicleId = 1L;
        String licencePlate = "ABCD123";

        Vehicle vehicle = Vehicle.builder()
                .id(vehicleId)
                .licencePlate(licencePlate)
                .user(User
                        .builder()
                        .id(1L)
                        .roles(Set.of(new Role(RoleEnum.ROLE_USER)))
                        .build())
                .vehicleType(VehicleTypeEnum.CAR.name())
                .build();

        given(vehicleService.getVehicle(vehicleId))
                .willReturn(vehicle);

        mockMvc.perform(get("/api/v1/vehicle/{vehicleId}", vehicleId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.licencePlate").value(vehicle.getLicencePlate()));
    }

    @Test
    @DisplayName("VehicleController - get vehicle by licence plate - should return 200 OK")
    void getVehicleByLicencePlate() throws Exception {
        long vehicleId = 1L;
        String licencePlate = "ABCD123";

        Vehicle vehicle = Vehicle.builder()
                .id(vehicleId)
                .licencePlate(licencePlate)
                .user(User
                        .builder()
                        .id(1L)
                        .roles(Set.of(new Role(RoleEnum.ROLE_USER)))
                        .build())
                .vehicleType(VehicleTypeEnum.CAR.name())
                .build();

        given(vehicleService.getVehicleByLicencePlate(licencePlate))
                .willReturn(vehicle);

        mockMvc.perform(get("/api/v1/vehicle/licence-plate/{licencePlate}", licencePlate))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.licencePlate").value(vehicle.getLicencePlate()));
    }

    @Test
    @DisplayName("VehicleController - delete vehicle - should return 202 ACCEPTED")
    void deleteVehicle() throws Exception {
        long vehicleId = 1L;
        mockMvc.perform(delete("/api/v1/vehicle/{vehicleId}", vehicleId))
                .andExpect(status().isAccepted());
    }
}
