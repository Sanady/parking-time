package com.parkingtime.authentication;

import com.parkingtime.authentication.configs.ApplicationConfig;
import com.parkingtime.authentication.configs.JwtService;
import com.parkingtime.authentication.models.Role;
import com.parkingtime.authentication.models.User;
import com.parkingtime.authentication.repositories.RoleRepository;
import com.parkingtime.authentication.repositories.UserRepository;
import com.parkingtime.authentication.services.AuthenticationService;
import com.parkingtime.common.enums.RoleEnum;
import com.parkingtime.common.exceptions.UserConflictException;
import com.parkingtime.common.requests.RegisterRequest;
import com.parkingtime.common.responses.MessageResponse;
import com.parkingtime.common.utilities.Randomizer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.parkingtime.common.enums.RoleEnum.ROLE_USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTests {
    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private ApplicationConfig applicationConfig;

    @InjectMocks
    private AuthenticationService authenticationService;

    private static Object[][] testDataProvider_signUpNewUser() {
        return new Object[][] {
                {
                        RegisterRequest
                                .builder()
                                .firstname(Randomizer.alphabeticGenerator(10))
                                .lastname(Randomizer.alphabeticGenerator(10))
                                .email(Randomizer.alphabeticGenerator(10) + "@gmail.com")
                                .password(Randomizer.alphabeticGenerator(1).toUpperCase() +
                                        Randomizer.alphabeticGenerator(9) +
                                        Randomizer.numericGenerator(2) +
                                        "@")
                                .roles(Set.of(ROLE_USER.name()))
                                .build(),
                        "User with user roles"
                },
                {
                        RegisterRequest
                                .builder()
                                .firstname(Randomizer.alphabeticGenerator(10))
                                .lastname(Randomizer.alphabeticGenerator(10))
                                .email(Randomizer.alphabeticGenerator(10) + "@gmail.com")
                                .password(Randomizer.alphabeticGenerator(1).toUpperCase() +
                                        Randomizer.alphabeticGenerator(9) +
                                        Randomizer.numericGenerator(2) +
                                        "@")
                                .roles(null)
                                .build(),
                        "User with null roles"
                },
        };
    }

    private static Object[][] testDataProvider_signUpUserWithConflicts() {
        return new Object[][] {
                {
                        "User email already exists"
                },
                {
                        "User firstname and lastname already exists"
                }
        };
    }

    @ParameterizedTest(name = "#{index} - {1}")
    @MethodSource("testDataProvider_signUpNewUser")
    @DisplayName("Register - sign up new user - success")
    void register_signUpNewUser_returnsMessageResponseObject(RegisterRequest request, String testName) {
        // Arrange
        when(userRepository.existsByEmail(request.getEmail()))
                .thenReturn(false);
        when(userRepository.existsByFirstnameAndLastname(request.getFirstname(), request.getLastname()))
                .thenReturn(false);
        if(request.getRoles() == null) {
            when(roleRepository.findByName(ROLE_USER))
                    .thenReturn(Optional.of(Role.builder().name(ROLE_USER).build()));
        } else {
            request.getRoles().forEach(role -> when(roleRepository.findByName(RoleEnum.valueOf(role)))
                    .thenReturn(Optional.of(Role.builder().name(RoleEnum.valueOf(role)).build())));
        }
        when(passwordEncoder.encode(request.getPassword()))
                .thenReturn(Randomizer.alphabeticGenerator(10));
        when(applicationConfig.isPasswordValid(request.getPassword()))
                .thenReturn(true);
        // Act
        MessageResponse messageResponse = authenticationService.register(request);
        // Assert
        assertEquals("User has been successfully registered!", messageResponse.message());
    }

    @ParameterizedTest(name = "#{index} - {0}")
    @MethodSource("testDataProvider_signUpUserWithConflicts")
    @DisplayName("Register - sign up user conflicts - failure")
    void register_signUpUserWithExistingConflict_returnsUserConflictException(
            String testCase
    ) {
        // Arrange
        RegisterRequest request = RegisterRequest
                .builder()
                .firstname(Randomizer.alphabeticGenerator(10))
                .lastname(Randomizer.alphabeticGenerator(10))
                .email(Randomizer.alphabeticGenerator(10) + "@gmail.com")
                .password(Randomizer.alphabeticGenerator(1).toUpperCase() +
                        Randomizer.alphabeticGenerator(9) +
                        Randomizer.numericGenerator(2) +
                        "@")
                .roles(Set.of(ROLE_USER.name()))
                .build();
        if(testCase.equals("User email already exists")) {
            when(userRepository.existsByEmail(request.getEmail()))
                    .thenReturn(true);
        } else {
            when(userRepository.existsByEmail(request.getEmail()))
                    .thenReturn(false);
            when(userRepository.existsByFirstnameAndLastname(request.getFirstname(), request.getLastname()))
                    .thenReturn(true);
        }
        // Act
        // Assert
        Assertions.assertThrows(UserConflictException.class, () -> authenticationService.register(request));
    }

    @Test
    @DisplayName("Register - sign up user with invalid password - failure")
    void register_signUpUserWithInvalidPassword_returnsIllegalArgumentException() {
        // Arrange
        RegisterRequest request = RegisterRequest
                .builder()
                .firstname(Randomizer.alphabeticGenerator(10))
                .lastname(Randomizer.alphabeticGenerator(10))
                .email(Randomizer.alphabeticGenerator(10) + "@gmail.com")
                .password(Randomizer.alphabeticGenerator(1).toUpperCase() +
                        Randomizer.alphabeticGenerator(9) +
                        Randomizer.numericGenerator(2) +
                        "@")
                .roles(Set.of(ROLE_USER.name()))
                .build();
        when(applicationConfig.isPasswordValid(request.getPassword()))
                .thenReturn(false);
        Assertions.assertThrows(IllegalArgumentException.class, () -> authenticationService.register(request));
    }

}
