package com.parkingtime.authentication.controllers;


import com.parkingtime.authentication.models.UserEmailVerification;
import com.parkingtime.authentication.models.UserResetPasswordToken;
import com.parkingtime.authentication.services.AuthenticationService;
import com.parkingtime.authentication.services.EmailService;
import com.parkingtime.authentication.services.UserEmailVerificationService;
import com.parkingtime.authentication.services.UserResetPasswordTokenService;
import com.parkingtime.common.enums.MessageEnum;
import com.parkingtime.common.requests.AuthenticationRequest;
import com.parkingtime.common.requests.RegisterRequest;
import com.parkingtime.common.requests.ResetPasswordRequest;
import com.parkingtime.common.responses.AuthenticationResponse;
import com.parkingtime.common.responses.MessageResponse;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.parkingtime.common.constants.ApplicationConstants.*;

@Slf4j
@RestController
@RequestMapping(AUTH)
@RequiredArgsConstructor
public class AuthenticationController {
    private final EmailService emailService;
    private final AuthenticationService authenticationService;
    private final UserEmailVerificationService userEmailVerificationService;
    private final UserResetPasswordTokenService userResetPasswordTokenService;

    @PostMapping(REGISTRATION)
    public ResponseEntity<MessageResponse> register(
            @Validated @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping(AUTHENTICATE)
    public ResponseEntity<AuthenticationResponse> authenticate(
            @Validated @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping(FORGET_PASSWORD)
    public ResponseEntity<MessageResponse> forgetPassword(@RequestParam String email) {
        UserResetPasswordToken resetPasswordToken = userResetPasswordTokenService
                .forgetPassword(email);
        try {
            log.info("Sending reset password email to {}", email);
            emailService.sendMail(resetPasswordToken.getUser().getEmail(),
                    "Reset password",
                    emailService.generateResetPasswordContent(resetPasswordToken));
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return ResponseEntity
                .accepted()
                .body(new MessageResponse(MessageEnum.PLEASE_CHECK_INBOX.getValue()));
    }

    @PostMapping(RESET_PASSWORD)
    public ResponseEntity<MessageResponse> resetPassword(@RequestBody ResetPasswordRequest request) {
        return ResponseEntity
                .accepted()
                .body(userResetPasswordTokenService.resetPassword(request));
    }

    @PostMapping(EMAIL_VERIFICATION)
    public ResponseEntity<MessageResponse> emailVerification(@RequestParam String email) {
        UserEmailVerification userEmailVerification = userEmailVerificationService
                .sendVerificationEmail(email);
        try {
            log.info("Sending verification code to {}", email);
            emailService.sendMail(email, "Verification Code", "Your verification code is " +
                    userEmailVerification.getCode());
        } catch (MessagingException e) {
            log.error("Error while sending email to {}", email);
            e.printStackTrace();
        }
        log.info("Verification code is sent to {}", email);
        return ResponseEntity
                .accepted()
                .body(new MessageResponse("Verification code is sent to your email address"));
    }

    @PostMapping(VERIFY_EMAIL)
    public ResponseEntity<MessageResponse> verifyEmail(@RequestParam String email, @RequestParam int code) {
        return ResponseEntity
                .accepted()
                .body(userEmailVerificationService.verifyEmail(email, code));
    }
}
