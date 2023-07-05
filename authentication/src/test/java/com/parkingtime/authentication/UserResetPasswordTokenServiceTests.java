package com.parkingtime.authentication;

import com.parkingtime.authentication.models.User;
import com.parkingtime.authentication.models.UserResetPasswordToken;
import com.parkingtime.authentication.repositories.UserRepository;
import com.parkingtime.authentication.repositories.UserResetPasswordHistoryRepository;
import com.parkingtime.authentication.repositories.UserResetPasswordTokenRepository;
import com.parkingtime.authentication.services.UserResetPasswordTokenService;
import com.parkingtime.common.exceptions.CoverUpMessageException;
import com.parkingtime.common.requests.ResetPasswordRequest;
import com.parkingtime.common.responses.MessageResponse;
import com.parkingtime.common.utilities.Randomizer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserResetPasswordTokenServiceTests {
    @Mock
    private UserResetPasswordHistoryRepository userResetPasswordHistoryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserResetPasswordTokenRepository userResetPasswordTokenRepository;

    @InjectMocks
    private UserResetPasswordTokenService userResetPasswordTokenService;

    @Test
    @DisplayName("forgetPassword - sent forget password request - success")
    void forgetPassword_sentForgetPasswordRequest_returnsUserResetPasswordTokenObject() {
        // Arrange
        String email = Randomizer.alphabeticGenerator(16) + "@gmail.com";
        String password = Randomizer.alphabeticGenerator(1).toUpperCase() +
                Randomizer.alphabeticGenerator(9) +
                Randomizer.numericGenerator(2) +
                "@";
        User user = User
                .builder()
                .email(email)
                .password(password)
                .build();
        given(userRepository.findByEmail(email)).willReturn(Optional.of(user));
        // Act
        UserResetPasswordToken userResetPasswordToken = userResetPasswordTokenService.forgetPassword(email);
        // Assert
        Assertions.assertNotNull(userResetPasswordToken);
        Assertions.assertEquals(userResetPasswordToken.getUser().getEmail(), user.getEmail());
    }

    @Test
    @DisplayName("forgetPassword - sent forget password request for user that not exist - failure")
    void forgetPassword_sentForgetPasswordRequestForUserThatNotExist_throwsNotFoundException() {
        // Arrange
        String email = Randomizer.alphabeticGenerator(16) + "@gmail.com";
        given(userRepository.findByEmail(email)).willReturn(Optional.empty());
        // Act
        // Assert
        Assertions.assertThrows(CoverUpMessageException.class, () -> userResetPasswordTokenService.forgetPassword(email));
    }

    @Test
    @DisplayName("forgetPassword - sent forget password request for user that already have reset password token record - failure")
    void forgetPassword_sentForgetPasswordRequestForUserThatAlreadyHaveResetPasswordTokenRecord_throwsCoverUpMessageException() {
        // Arrange
        String email = Randomizer.alphabeticGenerator(16) + "@gmail.com";
        String password = Randomizer.alphabeticGenerator(1).toUpperCase() +
                Randomizer.alphabeticGenerator(9) +
                Randomizer.numericGenerator(2) +
                "@";
        User user = User
                .builder()
                .email(email)
                .password(password)
                .build();
        given(userRepository.findByEmail(email)).willReturn(Optional.of(user));
        given(userResetPasswordTokenRepository.findUserResetPasswordTokenByUser(user))
                .willReturn(Optional.of(UserResetPasswordToken
                        .builder()
                        .usedToken(false)
                        .build()));
        // Act
        // Assert
        Assertions.assertThrows(CoverUpMessageException.class, () -> userResetPasswordTokenService.forgetPassword(email));
    }

    @Test
    @DisplayName("forgetPassword - sent forget password request for user that has sent request before 5 minutes - failure")
    void forgetPassword_sentForgetPasswordRequestForUserThatHasSentRequestBefore5Minutes_throwsCoverUpMessageException() {
        // Arrange
        String email = Randomizer.alphabeticGenerator(16) + "@gmail.com";
        String password = Randomizer.alphabeticGenerator(1).toUpperCase() +
                Randomizer.alphabeticGenerator(9) +
                Randomizer.numericGenerator(2) +
                "@";
        User user = User
                .builder()
                .email(email)
                .password(password)
                .build();
        given(userRepository.findByEmail(email)).willReturn(Optional.of(user));
        given(userResetPasswordTokenRepository.findUserResetPasswordTokenByUser(user))
                .willReturn(Optional.ofNullable(UserResetPasswordToken.builder()
                        .usedToken(false)
                        .createdAt(LocalDateTime.now())
                        .build()));
        // Act
        // Assert
        Assertions.assertThrows(CoverUpMessageException.class, () -> userResetPasswordTokenService.forgetPassword(email));
    }

    @Test
    @DisplayName("resetPassword - reset password - success")
    void resetPassword_resetPasswordWithCorrectData_returnsMessageResponseObject() {
        // Arrange
        // ---> Variables
        String email = Randomizer.alphabeticGenerator(16) + "@gmail.com";
        String password = Randomizer.alphabeticGenerator(1).toUpperCase() +
                Randomizer.alphabeticGenerator(9) +
                Randomizer.numericGenerator(2) +
                "@";
        String token = Randomizer.alphabeticGenerator(16);
        // ---> Objects
        User user = User
                .builder()
                .email(email)
                .password(password)
                .build();
        UserResetPasswordToken userResetPasswordToken = UserResetPasswordToken
                .builder()
                .user(user)
                .token(token)
                .usedToken(false)
                .createdAt(LocalDateTime.now())
                .build();
        ResetPasswordRequest request = ResetPasswordRequest
                .builder()
                .newPassword(password)
                .confirmPassword(password)
                .token(token)
                .build();
        // ---> Mocking
        given(userResetPasswordTokenRepository.findByToken(token)).willReturn(Optional.of(userResetPasswordToken));
        // Act
        MessageResponse messageResponse = userResetPasswordTokenService.resetPassword(request);
        // Assert
        Assertions.assertEquals("You have successfully reset password, try log in with new password.", messageResponse.message());
    }

    @Test
    @DisplayName("resetPassword - reset password with wrong token - failure")
    void resetPassword_resetPasswordWithWrongToken_throwsCoverUpMessageException() {
        // Arrange
        // ---> Variables
        String password = Randomizer.alphabeticGenerator(1).toUpperCase() +
                Randomizer.alphabeticGenerator(9) +
                Randomizer.numericGenerator(2) +
                "@";
        String token = Randomizer.alphabeticGenerator(16);
        // ---> Objects
        ResetPasswordRequest request = ResetPasswordRequest
                .builder()
                .newPassword(password)
                .confirmPassword(password)
                .token(token)
                .build();
        // ---> Mocking
        given(userResetPasswordTokenRepository.findByToken(token)).willReturn(Optional.empty());
        // Act
        // Assert
        Assertions.assertThrows(CoverUpMessageException.class, () -> userResetPasswordTokenService.resetPassword(request));
    }

    @Test
    @DisplayName("resetPassword - reset password with used token - failure")
    void resetPassword_resetPasswordWithUsedToken_throwsIllegalArgumentException() {
        // Arrange
        // ---> Variables
        String email = Randomizer.alphabeticGenerator(16) + "@gmail.com";
        String password = Randomizer.alphabeticGenerator(1).toUpperCase() +
                Randomizer.alphabeticGenerator(9) +
                Randomizer.numericGenerator(2) +
                "@";
        String token = Randomizer.alphabeticGenerator(16);
        // ---> Objects
        User user = User
                .builder()
                .email(email)
                .password(password)
                .build();
        UserResetPasswordToken userResetPasswordToken = UserResetPasswordToken
                .builder()
                .user(user)
                .token(token)
                .usedToken(true)
                .createdAt(LocalDateTime.now())
                .build();
        ResetPasswordRequest request = ResetPasswordRequest
                .builder()
                .newPassword(password)
                .confirmPassword(password)
                .token(token)
                .build();
        // ---> Mocking
        given(userResetPasswordTokenRepository.findByToken(token)).willReturn(Optional.of(userResetPasswordToken));
        // Act
        // Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> userResetPasswordTokenService.resetPassword(request));
    }

    @Test
    @DisplayName("resetPassword - reset password with wrong password - failure")
    void resetPassword_resetPasswordWithWrongPassword_throwsIllegalArgumentException() {
        // Arrange
        // ---> Variables
        String email = Randomizer.alphabeticGenerator(16) + "@gmail.com";
        String password = Randomizer.alphabeticGenerator(1).toUpperCase() +
                Randomizer.alphabeticGenerator(9) +
                Randomizer.numericGenerator(2) +
                "@";
        String token = Randomizer.alphabeticGenerator(16);
        // ---> Objects
        User user = User
                .builder()
                .email(email)
                .password(password)
                .build();
        UserResetPasswordToken userResetPasswordToken = UserResetPasswordToken
                .builder()
                .user(user)
                .token(token)
                .usedToken(false)
                .createdAt(LocalDateTime.now())
                .build();
        ResetPasswordRequest request = ResetPasswordRequest
                .builder()
                .newPassword(password + Randomizer.alphabeticGenerator(1))
                .confirmPassword(password)
                .token(token)
                .build();
        // ---> Mocking
        given(userResetPasswordTokenRepository.findByToken(token)).willReturn(Optional.of(userResetPasswordToken));
        // Act
        // Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> userResetPasswordTokenService.resetPassword(request));
    }
}
