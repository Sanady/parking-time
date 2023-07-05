package com.parkingtime.authentication;

import com.parkingtime.authentication.models.User;
import com.parkingtime.authentication.models.UserEmailVerification;
import com.parkingtime.authentication.repositories.UserEmailVerificationRepository;
import com.parkingtime.authentication.repositories.UserRepository;
import com.parkingtime.authentication.services.UserService;
import com.parkingtime.common.exceptions.NotFoundException;
import com.parkingtime.common.requests.ChangeUserPasswordRequest;
import com.parkingtime.common.responses.GetUserResponse;
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

import static com.parkingtime.common.enums.ErrorMessages.USER_EMAIL_DOES_NOT_HAVE_EMAIL_VERIFIED;
import static com.parkingtime.common.enums.ErrorMessages.USER_EMAIL_NOT_FOUND;
import static com.parkingtime.common.enums.ErrorMessages.USER_NEW_PASSWORD_DOES_NOT_MATCH;
import static com.parkingtime.common.enums.ErrorMessages.USER_OLD_PASSWORD_DOES_NOT_MATCH;
import static com.parkingtime.common.enums.MessageEnum.USER_CHANGED_PASSWORD;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceTests {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserEmailVerificationRepository userEmailVerificationRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("getUser - get user information - success")
    void getUser_getUserInformation_success() {
        // Arrange
        String email = Randomizer.alphabeticGenerator(16) + "@gmail.com";
        User user = User.builder()
                .id(1L)
                .email(email)
                .password(Randomizer.alphabeticGenerator(16))
                .build();

        UserEmailVerification userEmailVerification = UserEmailVerification.builder()
                .user(user)
                .code(Randomizer.numberGenerator(6))
                .build();

        given(userRepository.findByEmail(email)).willReturn(Optional.of(user));
        given(userEmailVerificationRepository.findUserEmailVerificationByUser(user))
                .willReturn(Optional.of(userEmailVerification));
        // Act
        GetUserResponse response = userService.getUser(email);
        // Assert
        Assertions.assertEquals(user.getId(), response.id());
        Assertions.assertEquals(user.getEmail(), response.email());
    }

    @Test
    @DisplayName("getUser - user not found - failure")
    void getUser_userNotFound_failure() {
        // Arrange
        String email = Randomizer.alphabeticGenerator(16) + "@gmail.com";
        given(userRepository.findByEmail(email)).willReturn(Optional.empty());
        // Act
        // Assert
        NotFoundException message = Assertions.assertThrows(NotFoundException.class, () -> userService.getUser(email));
        Assertions.assertEquals(USER_EMAIL_NOT_FOUND.getValue(), message.getMessage());
    }

    @Test
    @DisplayName("getUser - user does not have verified email - failure")
    void getUser_userDoesNotHaveVerifiedEmail_failure() {
        // Arrange
        String email = Randomizer.alphabeticGenerator(16) + "@gmail.com";
        User user = User.builder()
                .id(1L)
                .email(email)
                .password(Randomizer.alphabeticGenerator(16))
                .build();
        given(userRepository.findByEmail(email)).willReturn(Optional.of(user));
        given(userEmailVerificationRepository.findUserEmailVerificationByUser(user))
                .willReturn(Optional.empty());
        // Act
        // Assert
        NotFoundException message = Assertions.assertThrows(NotFoundException.class, () -> userService.getUser(email));
        Assertions.assertEquals(USER_EMAIL_DOES_NOT_HAVE_EMAIL_VERIFIED.getValue(), message.getMessage());
    }

    @Test
    @DisplayName("changeUserPassword - change user password - success")
    void changeUserPassword_changeUserPassword_success() {
        // Arrange
        String email = Randomizer.alphabeticGenerator(16) + "@gmail.com";
        String password = Randomizer.alphabeticGenerator(16);
        String newPassword = Randomizer.alphabeticGenerator(16);
        User user = User.builder()
                .id(1L)
                .email(email)
                .password(password)
                .build();
        ChangeUserPasswordRequest changeUserPasswordRequest = ChangeUserPasswordRequest.builder()
                .oldPassword(password)
                .newPassword(newPassword)
                .confirmPassword(newPassword)
                .build();
        UserEmailVerification userEmailVerification = UserEmailVerification.builder()
                .user(user)
                .active(true)
                .verifiedAt(LocalDateTime.now())
                .code(Randomizer.numberGenerator(6))
                .build();
        given(userRepository.findByEmail(email)).willReturn(Optional.of(user));
        given(passwordEncoder.matches(password, user.getPassword())).willReturn(true);
        given(userEmailVerificationRepository.findUserEmailVerificationByUser(user))
                .willReturn(Optional.of(userEmailVerification));
        // Act
        MessageResponse messageResponse = userService.changeUserPassword(email, changeUserPasswordRequest);
        // Assert
        Assertions.assertEquals(USER_CHANGED_PASSWORD.getValue(), messageResponse.message());
    }

    @Test
    @DisplayName("changeUserPassword - user not found - failure")
    void changeUserPassword_userNotFound_failure() {
        // Arrange
        String email = Randomizer.alphabeticGenerator(16) + "@gmail.com";
        String password = Randomizer.alphabeticGenerator(16);
        String newPassword = Randomizer.alphabeticGenerator(16);
        ChangeUserPasswordRequest changeUserPasswordRequest = ChangeUserPasswordRequest.builder()
                .oldPassword(password)
                .newPassword(newPassword)
                .confirmPassword(newPassword)
                .build();
        given(userRepository.findByEmail(email)).willReturn(Optional.empty());
        // Act
        // Assert
        NotFoundException message = Assertions.assertThrows(NotFoundException.class,
                () -> userService.changeUserPassword(email, changeUserPasswordRequest));
        Assertions.assertEquals(USER_EMAIL_NOT_FOUND.getValue(), message.getMessage());
    }

    @Test
    @DisplayName("changeUserPassword - user does not have verified email - failure")
    void changeUserPassword_userDoesNotHaveVerifiedEmail_failure() {
        // Arrange
        String email = Randomizer.alphabeticGenerator(16) + "@gmail.com";
        String password = Randomizer.alphabeticGenerator(16);
        String newPassword = Randomizer.alphabeticGenerator(16);
        User user = User.builder()
                .id(1L)
                .email(email)
                .password(password)
                .build();
        ChangeUserPasswordRequest changeUserPasswordRequest = ChangeUserPasswordRequest.builder()
                .oldPassword(password)
                .newPassword(newPassword)
                .confirmPassword(newPassword)
                .build();
        given(userRepository.findByEmail(email)).willReturn(Optional.of(user));
        given(userEmailVerificationRepository.findUserEmailVerificationByUser(user))
                .willReturn(Optional.empty());
        // Act
        // Assert
        NotFoundException message = Assertions.assertThrows(NotFoundException.class,
                () -> userService.changeUserPassword(email, changeUserPasswordRequest));
        Assertions.assertEquals(USER_EMAIL_DOES_NOT_HAVE_EMAIL_VERIFIED.getValue(), message.getMessage());
    }

    @Test
    @DisplayName("changeUserPassword - old password does not match - failure")
    void changeUserPassword_oldPasswordDoesNotMatch_failure() {
        // Arrange
        String email = Randomizer.alphabeticGenerator(16) + "@gmail.com";
        String password = Randomizer.alphabeticGenerator(16);
        String newPassword = Randomizer.alphabeticGenerator(16);
        User user = User.builder()
                .id(1L)
                .email(email)
                .password(password)
                .build();
        ChangeUserPasswordRequest changeUserPasswordRequest = ChangeUserPasswordRequest.builder()
                .oldPassword(password)
                .newPassword(newPassword)
                .confirmPassword(newPassword)
                .build();
        UserEmailVerification userEmailVerification = UserEmailVerification.builder()
                .user(user)
                .active(true)
                .verifiedAt(LocalDateTime.now())
                .code(Randomizer.numberGenerator(6))
                .build();
        given(userRepository.findByEmail(email)).willReturn(Optional.of(user));
        given(passwordEncoder.matches(password, user.getPassword())).willReturn(false);
        given(userEmailVerificationRepository.findUserEmailVerificationByUser(user))
                .willReturn(Optional.of(userEmailVerification));
        // Act
        // Assert
        IllegalArgumentException message = Assertions.assertThrows(IllegalArgumentException.class,
                () -> userService.changeUserPassword(email, changeUserPasswordRequest));
        Assertions.assertEquals(USER_OLD_PASSWORD_DOES_NOT_MATCH.getValue(), message.getMessage());
    }

    @Test
    @DisplayName("changeUserPassword - new password does not match - failure")
    void changeUserPassword_newPasswordDoesNotMatch_failure() {
        // Arrange
        String email = Randomizer.alphabeticGenerator(16) + "@gmail.com";
        String password = Randomizer.alphabeticGenerator(16);
        String newPassword = Randomizer.alphabeticGenerator(16);
        User user = User.builder()
                .id(1L)
                .email(email)
                .password(password)
                .build();
        ChangeUserPasswordRequest changeUserPasswordRequest = ChangeUserPasswordRequest.builder()
                .oldPassword(password)
                .newPassword(newPassword)
                .confirmPassword(Randomizer.alphabeticGenerator(16))
                .build();
        UserEmailVerification userEmailVerification = UserEmailVerification.builder()
                .user(user)
                .active(true)
                .verifiedAt(LocalDateTime.now())
                .code(Randomizer.numberGenerator(6))
                .build();
        given(userRepository.findByEmail(email)).willReturn(Optional.of(user));
        given(passwordEncoder.matches(password, user.getPassword())).willReturn(true);
        given(userEmailVerificationRepository.findUserEmailVerificationByUser(user))
                .willReturn(Optional.of(userEmailVerification));
        // Act
        // Assert
        IllegalArgumentException message = Assertions.assertThrows(IllegalArgumentException.class,
                () -> userService.changeUserPassword(email, changeUserPasswordRequest));
        Assertions.assertEquals(USER_NEW_PASSWORD_DOES_NOT_MATCH.getValue(), message.getMessage());
    }
}
