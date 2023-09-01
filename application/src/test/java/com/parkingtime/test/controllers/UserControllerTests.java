package com.parkingtime.test.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parkingtime.common.requests.ChangeUserPasswordRequest;
import com.parkingtime.common.responses.GetUserResponse;
import com.parkingtime.common.responses.MessageResponse;
import com.parkingtime.common.utilities.Randomizer;
import com.parkingtime.controllers.UserController;
import com.parkingtime.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTests {
    @Mock
    private UserService userService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        UserController userController = new UserController(userService);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    @DisplayName("UserController - get user - should return 200 OK")
    void getUser_getUserData_shouldReturn200OkStatusCode() throws Exception {
        String userEmail = "test@example.com";
        GetUserResponse expectedResponse = GetUserResponse.builder()
                .email(userEmail)
                .build();

        given(userService.getUser(anyString())).willReturn(expectedResponse);

        mockMvc.perform(get("/api/v1/user/{email}", userEmail))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(expectedResponse.email()));
    }

    @Test
    @DisplayName("UserController - change user password - should return 202 Accepted")
    void changeUserPassword_changeUserPassword_shouldReturn202AcceptedStatusCode() throws Exception {
        String userEmail = "test@example.com";
        String oldPassword = Randomizer.numericGenerator(10) + "aA!";
        String newPassword = oldPassword + "change";

        ChangeUserPasswordRequest request = ChangeUserPasswordRequest
                .builder()
                .oldPassword(oldPassword)
                .newPassword(newPassword)
                .confirmPassword(newPassword)
                .build();

        MessageResponse expectedResponse = new MessageResponse("User password has been successfully changed!");

        given(userService.changeUserPassword(anyString(), any(ChangeUserPasswordRequest.class)))
                .willReturn(expectedResponse);

        mockMvc.perform(patch("/api/v1/user/{email}/change-password", userEmail)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.message").value(expectedResponse.message()));
    }
}
