package com.parkingtime.authentication;

import com.parkingtime.authentication.models.Role;
import com.parkingtime.authentication.models.User;
import com.parkingtime.authentication.models.UserEmailVerification;
import com.parkingtime.authentication.repositories.UserEmailVerificationRepository;
import com.parkingtime.authentication.repositories.UserRepository;
import com.parkingtime.authentication.services.UserEmailVerificationService;
import com.parkingtime.common.enums.RoleEnum;
import com.parkingtime.common.exceptions.CoverUpMessageException;
import com.parkingtime.common.responses.MessageResponse;
import com.parkingtime.common.utilities.Randomizer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static com.parkingtime.common.enums.MessageEnum.EMAIL_IS_VERIFIED;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserEmailVerificationServiceTests {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserEmailVerificationRepository userEmailVerificationRepository;

    @InjectMocks
    private UserEmailVerificationService userEmailVerificationService;

    private static final String fakeEmail = "testmailing@gmail.com";
    private static User user;

    private static Object[][] testDataProvider() {
        return new Object[][]{
                {
                        UserEmailVerification.builder()
                                .user(user)
                                .code(Randomizer.numberGenerator(6))
                                .build(),
                        "Wrong code"
                },
                {
                        UserEmailVerification.builder()
                                .user(user)
                                .code(Randomizer.numberGenerator(6))
                                .verifiedAt(LocalDateTime.now())
                                .build(),
                        "Already verified email"
                }
        };
    }

    @BeforeAll
    static void beforeAll() {
        user = User.builder()
                .id(1L)
                .email(Randomizer.alphabeticGenerator(10) + "@gmail.com")
                .firstname(Randomizer.alphabeticGenerator(10))
                .lastname(Randomizer.alphabeticGenerator(10))
                .password(Randomizer.alphabeticGenerator(10))
                .roles(Set.of(new Role(RoleEnum.ROLE_USER)))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("sendVerificationEmail - send verification email - success")
    void sendVerificationEmail_sendEmailVerificationToUserEmail_returnsMessageResponse() {
        // Arrange
        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(user));
        // Act
        UserEmailVerification userEmailVerification = userEmailVerificationService
                .sendVerificationEmail(user.getEmail());
        // Assert
        Assertions.assertAll(() -> {
            Assertions.assertNotNull(userEmailVerification);
            Assertions.assertEquals(user.getEmail(), userEmailVerification.getUser().getEmail());
        });
    }

    @ParameterizedTest(name = "#{index} - Test with email = {0}")
    @DisplayName("sendVerificationEmail - send verification email - failure")
    @ValueSource(strings = {"", " ", "  ", fakeEmail})
    void sendVerificationEmail_sendEmailVerificationToUserEmail_returnsNullPointerException(String email) {
        // Arrange
        // Act
        // Assert
        NullPointerException exception = Assertions.assertThrows(NullPointerException.class,
                () -> userEmailVerificationService.sendVerificationEmail(email));
        if(email.equals(fakeEmail)) {
            Assertions.assertEquals("User with email " + email + " is not found", exception.getMessage());
        } else {
            Assertions.assertEquals("Email is not valid", exception.getMessage());
        }
    }


    @Test
    @DisplayName("verifyEmail - verify email - success")
    void verifyEmail_verifyEmail_returnsMessageResponse() {
        // Arrange
        int code = 123456;
        String email = Randomizer.alphabeticGenerator(10) + "@gmail.com";
        given(userRepository.findByEmail(anyString()))
                .willReturn(Optional.of(user));
        given(userEmailVerificationRepository.findUserEmailVerificationByUser(user))
                .willReturn(Optional.of(UserEmailVerification.builder()
                        .user(user)
                        .code(code)
                        .createdAt(LocalDateTime.now())
                        .build()));
        // Act
        MessageResponse messageResponse = userEmailVerificationService.verifyEmail(email, code);
        // Assert
        Assertions.assertAll(() -> {
            Assertions.assertNotNull(messageResponse);
            Assertions.assertEquals("Email is verified", messageResponse.message());
        });
    }

    @Test
    @DisplayName("verifyEmail - verify email - none existing email - failure")
    void verifyEmail_verifyEmail_returnsNullPointerException() {
        // Arrange
        int code = 123456;
        String email = Randomizer.alphabeticGenerator(10) + "@gmail.com";
        given(userRepository.findByEmail(anyString()))
                .willReturn(Optional.empty());
        // Act
        // Assert
        CoverUpMessageException exception = Assertions.assertThrows(CoverUpMessageException.class,
                () -> userEmailVerificationService.verifyEmail(email, code));
        Assertions.assertEquals(EMAIL_IS_VERIFIED.getValue(), exception.getMessage());
    }

    @ParameterizedTest(name = "#{index} - Test with code = {1}")
    @MethodSource("testDataProvider")
    @DisplayName("verifyEmail - verify email - wrong code - failure")
    void verifyEmail_verifyEmailWithInvalidData_returnsIllegalArgumentException(
            UserEmailVerification userEmailVerification,
            String message
    ) {
        // Arrange
        int code = 123456;
        String email = Randomizer.alphabeticGenerator(10) + "@gmail.com";
        given(userRepository.findByEmail(anyString()))
                .willReturn(Optional.of(user));
        given(userEmailVerificationRepository.findUserEmailVerificationByUser(user))
                .willReturn(Optional.of(userEmailVerification));
        // Act
        // Assert
        CoverUpMessageException exception = Assertions.assertThrows(CoverUpMessageException.class,
                () -> userEmailVerificationService.verifyEmail(email, code));
        Assertions.assertEquals(EMAIL_IS_VERIFIED.getValue(), exception.getMessage());
    }
}
