package com.parkingtime.authentication.services;

import com.parkingtime.authentication.models.User;
import com.parkingtime.authentication.models.UserEmailVerification;
import com.parkingtime.authentication.repositories.UserEmailVerificationRepository;
import com.parkingtime.authentication.repositories.UserRepository;
import com.parkingtime.common.exceptions.NotFoundException;
import com.parkingtime.common.requests.ChangeUserPasswordRequest;
import com.parkingtime.common.responses.GetUserResponse;
import com.parkingtime.common.responses.MessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.parkingtime.common.enums.ErrorMessages.USER_EMAIL_DOES_NOT_HAVE_EMAIL_VERIFIED;
import static com.parkingtime.common.enums.ErrorMessages.USER_EMAIL_NOT_FOUND;
import static com.parkingtime.common.enums.ErrorMessages.USER_NEW_PASSWORD_DOES_NOT_MATCH;
import static com.parkingtime.common.enums.ErrorMessages.USER_OLD_PASSWORD_DOES_NOT_MATCH;
import static com.parkingtime.common.enums.MessageEnum.USER_CHANGED_PASSWORD;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserEmailVerificationRepository userEmailVerificationRepository;
    private final PasswordEncoder passwordEncoder;

    public GetUserResponse getUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("User with email {} does not exists", email);
                    return new NotFoundException(USER_EMAIL_NOT_FOUND.getValue());
                });
        UserEmailVerification userEmailVerification = userEmailVerificationRepository.findUserEmailVerificationByUser(user)
                .orElseThrow(() -> {
                    log.error("User with email {} does not have email verification", email);
                    return new NotFoundException(USER_EMAIL_DOES_NOT_HAVE_EMAIL_VERIFIED.getValue());
                });
        log.info("Get user has been called for user with email {}", email);
        return GetUserResponse
                .builder()
                .id(user.getId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .verified(userEmailVerification != null ? userEmailVerification.getActive() : Boolean.FALSE)
                .verifiedAt(userEmailVerification != null ? userEmailVerification.getVerifiedAt() : null)
                .build();
    }

    public MessageResponse changeUserPassword(String email, ChangeUserPasswordRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(USER_EMAIL_NOT_FOUND.getValue()));

        UserEmailVerification userEmailVerification = userEmailVerificationRepository
                .findUserEmailVerificationByUser(user)
                .orElseThrow(() -> new NotFoundException(USER_EMAIL_DOES_NOT_HAVE_EMAIL_VERIFIED.getValue()));

        if(Boolean.TRUE.equals(!userEmailVerification.getActive()) || userEmailVerification.getVerifiedAt() == null) {
            throw new NotFoundException(USER_EMAIL_DOES_NOT_HAVE_EMAIL_VERIFIED.getValue());
        } else if(!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException(USER_OLD_PASSWORD_DOES_NOT_MATCH.getValue());
        } else if(!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException(USER_NEW_PASSWORD_DOES_NOT_MATCH.getValue());
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        log.info("User with email {} has changed his password", email);
        return MessageResponse
                .builder()
                .message(USER_CHANGED_PASSWORD.getValue())
                .build();
    }
}
