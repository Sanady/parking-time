package com.parkingtime.authentication.controllers;

import com.parkingtime.authentication.services.UserService;
import com.parkingtime.common.requests.ChangeUserPasswordRequest;
import com.parkingtime.common.responses.GetUserResponse;
import com.parkingtime.common.responses.MessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.parkingtime.common.constants.ApplicationConstants.*;


@Slf4j
@RestController
@RequestMapping(USER)
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PreAuthorize(value = "hasRole('ROLE_USER')")
    @GetMapping(GET_USER)
    public ResponseEntity<GetUserResponse> getUser(
            @PathVariable String email) {
        return ResponseEntity.ok(userService.getUser(email));
    }

    @PreAuthorize(value = "hasRole('ROLE_USER')")
    @PatchMapping(CHANGE_USER_PASSWORD)
    public ResponseEntity<MessageResponse> changeUserPassword(@PathVariable String email,
                                                             @Validated ChangeUserPasswordRequest request) {
        return null;
    }
}
