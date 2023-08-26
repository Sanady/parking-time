package com.parkingtime.authentication.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parkingtime.authentication.models.User;
import com.parkingtime.authentication.models.UserEmailVerification;
import com.parkingtime.authentication.models.UserResetPasswordToken;
import com.parkingtime.authentication.services.AuthenticationService;
import com.parkingtime.authentication.services.EmailService;
import com.parkingtime.authentication.services.UserEmailVerificationService;
import com.parkingtime.authentication.services.UserResetPasswordTokenService;
import com.parkingtime.common.enums.MessageEnum;
import com.parkingtime.common.enums.RoleEnum;
import com.parkingtime.common.requests.AuthenticationRequest;
import com.parkingtime.common.requests.RegisterRequest;
import com.parkingtime.common.requests.ResetPasswordRequest;
import com.parkingtime.common.responses.AuthenticationResponse;
import com.parkingtime.common.responses.MessageResponse;
import com.parkingtime.common.utilities.Randomizer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthenticationControllerTests {
    @Mock
    private EmailService emailService;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private UserEmailVerificationService userEmailVerificationService;

    @Mock
    private UserResetPasswordTokenService userResetPasswordTokenService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        AuthenticationController authenticationController = new AuthenticationController(emailService,
                authenticationService,
                userEmailVerificationService,
                userResetPasswordTokenService);
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
    }

    @Test
    @DisplayName("AuthorizationController - register - should return 200 OK")
    void register() throws Exception {
        RegisterRequest registerRequest = RegisterRequest
                .builder()
                .email(Randomizer.alphabeticGenerator(10) + "@gmail.com")
                .password(Randomizer.alphabeticGenerator(10))
                .firstname(Randomizer.alphabeticGenerator(1).toUpperCase() + Randomizer.alphabeticGenerator(9))
                .lastname(Randomizer.alphabeticGenerator(1).toUpperCase() + Randomizer.alphabeticGenerator(9))
                .roles(Set.of(RoleEnum.ROLE_USER.name()))
                .build();
        MessageResponse messageResponse = MessageResponse
                .builder()
                .message("User has been successfully registered!")
                .build();
        given(authenticationService.register(any(RegisterRequest.class))).willReturn(messageResponse);
        mockMvc.perform(post("/api/v1/auth/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(messageResponse.message()));
    }

    @Test
    @DisplayName("AuthorizationController - authenticate - should return 200 OK")
    void authenticate() throws Exception {
        AuthenticationRequest authenticationRequest = AuthenticationRequest
                .builder()
                .email(Randomizer.alphabeticGenerator(10) + "@gmail.com")
                .password(Randomizer.alphabeticGenerator(10))
                .build();
        AuthenticationResponse authenticationResponse = AuthenticationResponse
                .builder()
                .token(Randomizer.alphabeticGenerator(10))
                .build();
        given(authenticationService.authenticate(any(AuthenticationRequest.class))).willReturn(authenticationResponse);
        mockMvc.perform(post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(authenticationRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(authenticationResponse.token()));
    }

    @Test
    @DisplayName("AuthorizationController - forgetPassword - should return 202 Accepted")
    void forgetPassword() throws Exception {
        String email = Randomizer.alphabeticGenerator(10) + "@gmail.com";
        User user = User
                .builder()
                .email(email)
                .build();
        UserResetPasswordToken userResetPasswordToken = UserResetPasswordToken
                .builder()
                .user(user)
                .token(Randomizer.alphabeticGenerator(10))
                .build();
        given(userResetPasswordTokenService.forgetPassword(anyString())).willReturn(userResetPasswordToken);
        MvcResult mvcResult = mockMvc.perform(post("/api/v1/auth/forget-password")
                        .param("email", email))
                .andExpect(status().isAccepted())
                .andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        MessageResponse messageResponse = new ObjectMapper().readValue(contentAsString, MessageResponse.class);
        Assertions.assertEquals(messageResponse.message(), MessageEnum.PLEASE_CHECK_INBOX.getValue());
    }

    @Test
    @DisplayName("AuthorizationController - resetPassword - should return 202 Accepted")
    void resetPassword() throws Exception {
        String newPassword = Randomizer.alphabeticGenerator(10);
        ResetPasswordRequest resetPasswordRequest = ResetPasswordRequest
                .builder()
                .token(Randomizer.alphabeticGenerator(10))
                .newPassword(newPassword)
                .confirmPassword(newPassword)
                .build();
        MessageResponse messageResponse = MessageResponse
                .builder()
                .message("Password has been successfully reset!")
                .build();
        given(userResetPasswordTokenService.resetPassword(any(ResetPasswordRequest.class))).willReturn(messageResponse);
        mockMvc.perform(post("/api/v1/auth/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(resetPasswordRequest)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.message").value(messageResponse.message()));
    }

    @Test
    @DisplayName("AuthorizationController - emailVerification - should return 202 Accepted")
    void emailVerification() throws Exception {
        String email = Randomizer.alphabeticGenerator(10) + "@gmail.com";
        UserEmailVerification userEmailVerification = UserEmailVerification
                .builder()
                .user(User
                        .builder()
                        .email(email)
                        .build())
                .code(123456)
                .build();
        given(userEmailVerificationService.sendVerificationEmail(anyString())).willReturn(userEmailVerification);
        MvcResult mvcResult = mockMvc.perform(post("/api/v1/auth/email-verification")
                        .param("email", email))
                .andExpect(status().isAccepted())
                .andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        MessageResponse messageResponse = new ObjectMapper().readValue(contentAsString, MessageResponse.class);
        Assertions.assertEquals("Verification code is sent to your email address", messageResponse.message());
    }

    @Test
    @DisplayName("AuthorizationController - verifyEmail - should return 202 Accepted")
    void verifyEmail() throws Exception {
        String email = Randomizer.alphabeticGenerator(10) + "@gmail.com";
        int code = 123456;
        MessageResponse messageResponse = MessageResponse
                .builder()
                .message("Email has been successfully verified!")
                .build();
        given(userEmailVerificationService.verifyEmail(anyString(), anyInt())).willReturn(messageResponse);
        mockMvc.perform(post("/api/v1/auth/verify-email")
                        .param("email", email)
                        .param("code", String.valueOf(code)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.message").value(messageResponse.message()));
    }
}
