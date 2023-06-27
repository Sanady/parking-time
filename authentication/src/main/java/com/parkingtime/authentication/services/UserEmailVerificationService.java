package com.parkingtime.authentication.services;

import com.parkingtime.authentication.models.User;
import com.parkingtime.authentication.models.UserEmailVerification;
import com.parkingtime.authentication.repositories.UserEmailVerificationRepository;
import com.parkingtime.authentication.repositories.UserRepository;
import com.parkingtime.common.exceptions.CoverUpMessageException;
import com.parkingtime.common.responses.MessageResponse;
import com.parkingtime.common.utilities.Randomizer;
import com.parkingtime.common.utilities.TimeUtilities;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserEmailVerificationService {
    private final UserRepository userRepository;
    private final UserEmailVerificationRepository userEmailVerificationRepository;

    public UserEmailVerification sendVerificationEmail(String email) {
        if(email == null || email.isEmpty() || email.isBlank()) {
            log.warn("Email is not valid");
            throw new NullPointerException("Email is not valid");
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
                    throw new CoverUpMessageException("Email is verified");
                });

        UserEmailVerification userEmailVerification = userEmailVerificationRepository
                .findUserEmailVerificationByUser(user)
                .orElseThrow(() -> {
                    log.warn("User is not found");
                    throw new CoverUpMessageException("Email is verified");
                });

        if(userEmailVerification.getCode() != code || !userEmailVerification.getActive()) {
            log.warn("Code is not valid");
            throw new CoverUpMessageException("Email is verified");
        }

        if(TimeUtilities.isAtLeastFiveMinutesAgo(userEmailVerification.getCreatedAt())) {
            userEmailVerification.setActive(false);
            userEmailVerificationRepository.save(userEmailVerification);
            log.warn("Code has expired about 5 minutes ago or even longer");
            throw new CoverUpMessageException("Email is verified");
        }

        if(userEmailVerification.getVerifiedAt() != null) {
            log.warn("Email is already verified");
            throw new CoverUpMessageException("Email is verified");
        }

        userEmailVerification.setVerifiedAt(LocalDateTime.now());
        userEmailVerificationRepository.save(userEmailVerification);
        return new MessageResponse("Email is verified");
    }
}
