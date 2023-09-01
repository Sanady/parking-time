package com.parkingtime.services;

import com.parkingtime.models.User;
import com.parkingtime.models.UserEmailVerification;
import com.parkingtime.repositories.UserEmailVerificationRepository;
import com.parkingtime.repositories.UserRepository;
import com.parkingtime.common.exceptions.CoverUpMessageException;
import com.parkingtime.common.responses.MessageResponse;
import com.parkingtime.common.utilities.Randomizer;
import com.parkingtime.common.utilities.TimeUtilities;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.parkingtime.common.enums.ErrorMessages.NOT_VALID_EMAIL;
import static com.parkingtime.common.enums.MessageEnum.EMAIL_IS_VERIFIED;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserEmailVerificationService {
    private final UserRepository userRepository;
    private final UserEmailVerificationRepository userEmailVerificationRepository;

    public UserEmailVerification sendVerificationEmail(String email) {
        if(email == null || email.isEmpty() || email.isBlank()) {
            log.warn(NOT_VALID_EMAIL.getValue());
            throw new NullPointerException(NOT_VALID_EMAIL.getValue());
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("User with email {} is not found", email);
                    return new NullPointerException("User with email " + email + " is not found");
                });

        int code = Randomizer.numberGenerator(6);

        UserEmailVerification userEmailVerification = UserEmailVerification.builder()
                .user(user)
                .code(code)
                .active(true)
                .createdAt(LocalDateTime.now())
                .verifiedAt(null)
                .build();
        userEmailVerificationRepository.save(userEmailVerification);
        return userEmailVerification;
    }

    public MessageResponse verifyEmail(String email, int code) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("User with email {} is not found", email);
                    return new CoverUpMessageException(EMAIL_IS_VERIFIED.getValue());
                });

        UserEmailVerification userEmailVerification = userEmailVerificationRepository
                .findUserEmailVerificationByUser(user)
                .orElseThrow(() -> {
                    log.warn("User is not found");
                    return new CoverUpMessageException(EMAIL_IS_VERIFIED.getValue());
                });

        if(userEmailVerification.getCode() != code || Boolean.FALSE.equals(userEmailVerification.getActive())) {
            log.warn("Code is not valid or is not active");
            throw new CoverUpMessageException(EMAIL_IS_VERIFIED.getValue());
        }

        if(TimeUtilities.isAtLeastFiveMinutesAgo(userEmailVerification.getCreatedAt())) {
            userEmailVerification.setActive(false);
            userEmailVerificationRepository.save(userEmailVerification);
            log.warn("Code has expired about 5 minutes ago or even longer");
            throw new CoverUpMessageException(EMAIL_IS_VERIFIED.getValue());
        }

        if(userEmailVerification.getVerifiedAt() != null) {
            log.warn("Email is already verified");
            throw new CoverUpMessageException(EMAIL_IS_VERIFIED.getValue());
        }

        userEmailVerification.setVerifiedAt(LocalDateTime.now());
        userEmailVerificationRepository.save(userEmailVerification);
        return new MessageResponse(EMAIL_IS_VERIFIED.getValue());
    }
}
