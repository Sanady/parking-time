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

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserEmailVerificationRepository userEmailVerificationRepository;
    private final PasswordEncoder passwordEncoder;

    public GetUserResponse getUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User with this email does not exists!"));
        log.info("Get user has been called for user with email {}", email);
        return GetUserResponse
                .builder()
                .id(user.getId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    public MessageResponse changeUserPassword(String email, ChangeUserPasswordRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User with this email does not exists!"));

        UserEmailVerification userEmailVerification = userEmailVerificationRepository
                .findUserEmailVerificationByUser(user)
                .orElseThrow(() -> new NotFoundException("User email is not verified!"));

        if(!userEmailVerification.getActive() || userEmailVerification.getVerifiedAt() == null) {
            throw new NotFoundException("User email is not verified!");
        }

        if(!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("User old password does not match with the password from the request!");
        }

        if(!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("User new password and confirmation password does not matches!");
        }
        return null;
    }
}
