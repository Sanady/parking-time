package com.parkingtime.test.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parkingtime.common.enums.MessageEnum;
import com.parkingtime.common.enums.RoleEnum;
import com.parkingtime.common.requests.AuthenticationRequest;
import com.parkingtime.common.requests.RegisterRequest;
import com.parkingtime.common.requests.ResetPasswordRequest;
import com.parkingtime.common.responses.AuthenticationResponse;
import com.parkingtime.common.responses.MessageResponse;
import com.parkingtime.common.utilities.Randomizer;
import com.parkingtime.controllers.AuthenticationController;
import com.parkingtime.models.User;
import com.parkingtime.models.UserEmailVerification;
import com.parkingtime.models.UserResetPasswordToken;
import com.parkingtime.services.AuthenticationService;
import com.parkingtime.services.EmailService;
import com.parkingtime.services.UserEmailVerificationService;
import com.parkingtime.services.UserResetPasswordTokenService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
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

    private static Object[][] testDataProviderForRegisterEndpoint() {
        return new Object[][] {
                {
                        RegisterRequest
                                .builder()
                                .email(Randomizer.alphabeticGenerator(10))
                                .password(Randomizer.passwordGenerator())
                                .firstname(Randomizer.alphabeticGenerator(1).toUpperCase() + Randomizer.alphabeticGenerator(9))
                                .lastname(Randomizer.alphabeticGenerator(1).toUpperCase() + Randomizer.alphabeticGenerator(9))
                                .roles(Set.of(RoleEnum.ROLE_USER.name()))
                                .build(),
                        "Email is not valid"
                },
                {
                        RegisterRequest
                                .builder()
                                .email(Randomizer.alphabeticGenerator(10) + "@gmail.com")
                                .password(Randomizer.passwordGenerator())
                                .firstname(Randomizer.alphabeticGenerator(9))
                                .lastname(Randomizer.alphabeticGenerator(1).toUpperCase() + Randomizer.alphabeticGenerator(9))
                                .roles(Set.of(RoleEnum.ROLE_USER.name()))
                                .build(),
                        "Firstname is not valid"
                },
                {
                        RegisterRequest
                                .builder()
                                .email(Randomizer.alphabeticGenerator(10) + "@gmail.com")
                                .password(Randomizer.passwordGenerator())
                                .firstname(Randomizer.alphabeticGenerator(1).toUpperCase() + Randomizer.alphabeticGenerator(9))
                                .lastname(Randomizer.alphabeticGenerator(9))
                                .roles(Set.of(RoleEnum.ROLE_USER.name()))
                                .build(),
                        "Lastname is not valid"
                },
                {
                        RegisterRequest
                                .builder()
                                .email(null)
                                .password(Randomizer.passwordGenerator())
                                .firstname(Randomizer.alphabeticGenerator(1).toUpperCase() + Randomizer.alphabeticGenerator(9))
                                .lastname(Randomizer.alphabeticGenerator(1).toUpperCase() + Randomizer.alphabeticGenerator(9))
                                .roles(Set.of(RoleEnum.ROLE_USER.name()))
                                .build(),
                        "Email is null"
                },
                {
                        RegisterRequest
                                .builder()
                                .email(Randomizer.alphabeticGenerator(10) + "@gmail.com")
                                .password(Randomizer.passwordGenerator())
                                .firstname(null)
                                .lastname(Randomizer.alphabeticGenerator(1).toUpperCase() + Randomizer.alphabeticGenerator(9))
                                .roles(Set.of(RoleEnum.ROLE_USER.name()))
                                .build(),
                        "Firstname is null"
                },
                {
                        RegisterRequest
                                .builder()
                                .email(Randomizer.alphabeticGenerator(10) + "@gmail.com")
                                .password(Randomizer.passwordGenerator())
                                .firstname(Randomizer.alphabeticGenerator(1).toUpperCase() + Randomizer.alphabeticGenerator(9))
                                .lastname(null)
                                .roles(Set.of(RoleEnum.ROLE_USER.name()))
                                .build(),
                        "Lastname is null"
                },
                {
                        RegisterRequest
                                .builder()
                                .email(Randomizer.alphabeticGenerator(10) + "@gmail.com")
                                .password(null)
                                .firstname(Randomizer.alphabeticGenerator(1).toUpperCase() + Randomizer.alphabeticGenerator(9))
                                .lastname(Randomizer.alphabeticGenerator(1).toUpperCase() + Randomizer.alphabeticGenerator(9))
                                .roles(Set.of(RoleEnum.ROLE_USER.name()))
                                .build(),
                        "Password is null"
                },
                {
                        RegisterRequest
                                .builder()
                                .email(Randomizer.alphabeticGenerator(10) + "@gmail.com")
                                .password(Randomizer.alphabeticGenerator(10))
                                .firstname(Randomizer.alphabeticGenerator(1).toUpperCase() + Randomizer.alphabeticGenerator(9))
                                .lastname(Randomizer.alphabeticGenerator(1).toUpperCase() + Randomizer.alphabeticGenerator(9))
                                .roles(Set.of(RoleEnum.ROLE_USER.name()))
                                .build(),
                        "Password is not valid"
                }
        };
    }

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
    void register_signUpUserThroughController_shouldReturn200OkStatus() throws Exception {
        RegisterRequest registerRequest = RegisterRequest
                .builder()
                .email(Randomizer.alphabeticGenerator(10) + "@gmail.com")
                .password(Randomizer.passwordGenerator())
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
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value(messageResponse.message()));
    }

    @ParameterizedTest(name = "#{index} - Run test with register request = {1}")
    @MethodSource("testDataProviderForRegisterEndpoint")
    @DisplayName("AuthorizationController - register - should return 400 BAD REQUEST")
    void register_signUpUserWithInvalidRequest_shouldReturn400BadRequest(RegisterRequest registerRequest, String reason)
            throws Exception {
        mockMvc.perform(post("/api/v1/auth/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("AuthorizationController - authenticate - should return 200 OK")
    void authenticate_signInUserWithValidData_shouldReturn200OkStatusCode() throws Exception {
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
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.token").value(authenticationResponse.token()));
    }

    @Test
    @DisplayName("AuthorizationController - forgetPassword - should return 202 Accepted")
    void forgetPassword_startProcessOfForgetPassword_shouldReturn202AcceptedStatusCode() throws Exception {
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
    void resetPassword_resetUserPassword_shouldReturn202AcceptedStatusCode() throws Exception {
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
    void emailVerification_sentEmailVerification_shouldReturn202AcceptedStatusCode() throws Exception {
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
    void verifyEmail_verifyUserEmailAddress_shouldReturn202AcceptedStatusCode() throws Exception {
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
