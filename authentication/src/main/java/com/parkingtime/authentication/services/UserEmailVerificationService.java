package com.parkingtime.authentication.services;

import com.parkingtime.authentication.models.User;
import com.parkingtime.authentication.models.UserEmailVerification;
import com.parkingtime.authentication.repositories.UserEmailVerificationRepository;
import com.parkingtime.authentication.repositories.UserRepository;
import com.parkingtime.common.responses.MessageResponse;
import com.parkingtime.common.utilities.Randomizer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserEmailVerificationService {
    private final UserRepository userRepository;
    private final EmailService emailService;
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
                    return new NullPointerException("User with email " + email + " is not found");
                });

        UserEmailVerification userEmailVerification = userEmailVerificationRepository
                .findUserEmailVerificationByUser(user)
                .orElseThrow(() -> {
                    log.warn("User is not found");
                    return new NullPointerException("User is not found");
                });

        if(userEmailVerification.getCode() != code) {
            log.warn("Code is not valid");
            throw new NullPointerException("Code is not valid");
        }

        if(userEmailVerification.getVerifiedAt() != null) {
            log.warn("Email is already verified");
            throw new NullPointerException("Email is already verified");
        }

        userEmailVerification.setVerifiedAt(LocalDateTime.now());
        userEmailVerificationRepository.save(userEmailVerification);
        return new MessageResponse("Email is verified");
    }
}
