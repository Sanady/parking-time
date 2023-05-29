package com.parkingtime.authentication.services;


import com.parkingtime.authentication.models.User;
import com.parkingtime.authentication.models.UserResetPasswordHistory;
import com.parkingtime.authentication.models.UserResetPasswordToken;
import com.parkingtime.authentication.repositories.UserRepository;
import com.parkingtime.authentication.repositories.UserResetPasswordHistoryRepository;
import com.parkingtime.authentication.repositories.UserResetPasswordTokenRepository;
import com.parkingtime.common.enums.ErrorMessages;
import com.parkingtime.common.enums.MessageEnum;
import com.parkingtime.common.enums.ResetPasswordMethodEnum;
import com.parkingtime.common.exceptions.CoverUpMessageException;
import com.parkingtime.common.requests.ResetPasswordRequest;
import com.parkingtime.common.responses.MessageResponse;
import com.parkingtime.common.utilities.Randomizer;
import com.parkingtime.common.utilities.TimeUtilities;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserResetPasswordTokenService {
    private final UserResetPasswordHistoryRepository userResetPasswordHistoryRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserResetPasswordTokenRepository userResetPasswordTokenRepository;

    public UserResetPasswordToken forgetPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("Non-existing user with email {} is trying to send request of forget password", email);
                    return new CoverUpMessageException(MessageEnum.PLEASE_CHECK_INBOX.getValue());
                });

        if(userResetPasswordTokenRepository.findUserResetPasswordTokenByUser(user).isPresent()) {
            UserResetPasswordToken alreadyExistingToken = userResetPasswordTokenRepository.findUserResetPasswordTokenByUser(user)
                    .orElseThrow(() -> {
                        log.error("User with {} email is trying to abuse the forget password request", user.getEmail());
                        return new CoverUpMessageException(MessageEnum.PLEASE_CHECK_INBOX.getValue());
                    });

            if(Boolean.TRUE.equals(userResetPasswordTokenRepository.countByUser(user) > 0 &&
                    !alreadyExistingToken.getUsedToken()) &&
                    !TimeUtilities.isAtLeastFiveMinutesAgo(alreadyExistingToken.getCreatedAt())) {
                log.error("User with email {} is trying to input expired token or already used token", user.getEmail());
                throw new CoverUpMessageException(MessageEnum.PLEASE_CHECK_INBOX.getValue());
            }
        }

        String token = Randomizer.numericGenerator(6);
        UserResetPasswordToken resetPasswordToken = UserResetPasswordToken
                .builder()
                .user(user)
                .token(token)
                .method(ResetPasswordMethodEnum.METHOD_EMAIL.getValue())
                .usedToken(false)
                .createdAt(LocalDateTime.now())
                .build();

        userResetPasswordTokenRepository.save(resetPasswordToken);
        log.info("Forget password flow has been created by email: {}", user.getEmail());
        return resetPasswordToken;
    }

    public MessageResponse resetPassword(ResetPasswordRequest request) {
        UserResetPasswordToken userResetPasswordToken = userResetPasswordTokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> {
                    log.error("User is trying to input non-existing token.");
                    return new CoverUpMessageException(MessageEnum.SUCCESSFULLY_RESET_PASSWORD.getValue());
                });

        if(Boolean.TRUE.equals(userResetPasswordToken.getUsedToken()) ||
                Boolean.TRUE.equals(TimeUtilities.isAtLeastFiveMinutesAgo(userResetPasswordToken.getCreatedAt()))) {
            throw new IllegalArgumentException(ErrorMessages.TOKEN_EXPIRED.getValue());
        }
        if(!request.getNewPassword().equals(request.getConfirmPassword()))
            throw new IllegalArgumentException(ErrorMessages.PASSWORD_MISMATCH.getValue());

        User user = userResetPasswordToken.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        userResetPasswordToken.setUsedToken(true);
        userResetPasswordToken.setConsumedAt(LocalDateTime.now());
        userResetPasswordTokenRepository.save(userResetPasswordToken);

        UserResetPasswordHistory userResetPasswordHistory = UserResetPasswordHistory
                .builder()
                .user(user)
                .method(ResetPasswordMethodEnum.METHOD_EMAIL)
                .createdAt(LocalDateTime.now())
                .build();
        userResetPasswordHistoryRepository.save(userResetPasswordHistory);
        log.info("User with email {} has reset password", user.getEmail());
        return new MessageResponse(MessageEnum.SUCCESSFULLY_RESET_PASSWORD.getValue());
    }
}
