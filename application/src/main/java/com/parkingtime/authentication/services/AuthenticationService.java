package com.parkingtime.authentication.services;

import com.parkingtime.authentication.configs.ApplicationConfig;
import com.parkingtime.authentication.configs.JwtService;
import com.parkingtime.authentication.models.Role;
import com.parkingtime.authentication.models.User;
import com.parkingtime.authentication.repositories.RoleRepository;
import com.parkingtime.authentication.repositories.UserRepository;
import com.parkingtime.common.enums.ErrorMessages;
import com.parkingtime.common.enums.RoleEnum;
import com.parkingtime.common.exceptions.LocalAuthenticationException;
import com.parkingtime.common.exceptions.UserConflictException;
import com.parkingtime.common.requests.AuthenticationRequest;
import com.parkingtime.common.requests.RegisterRequest;
import com.parkingtime.common.responses.AuthenticationResponse;
import com.parkingtime.common.responses.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ApplicationConfig applicationConfig;

    @Transactional
    public MessageResponse register(RegisterRequest request) {
        if(Boolean.TRUE.equals(userRepository.existsByEmail(request.getEmail()))) {
            throw new UserConflictException(ErrorMessages.USER_ALREADY_EXISTS_IN_SYSTEM.getValue());
        }

        if(Boolean.TRUE.equals(userRepository.existsByFirstnameAndLastname(request.getFirstname(),
                request.getLastname()))) {
            throw new UserConflictException(ErrorMessages.USER_ALREADY_EXISTS_IN_SYSTEM.getValue());
        }

        if(Boolean.TRUE.equals(request.getPassword() == null) ||
                Boolean.FALSE.equals(applicationConfig.isPasswordValid(request.getPassword()))) {
            throw new IllegalArgumentException("Password does not meet minimum requirements to be accepted.");
        }

        Set<String> strRoles = request.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(RoleEnum.ROLE_USER)
                    .orElseThrow(() -> new IllegalArgumentException(ErrorMessages.ROLE_NOT_FOUND.getValue()));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin" -> {
                        Role adminRole = roleRepository.findByName(RoleEnum.ROLE_ADMIN)
                                .orElseThrow(() -> new IllegalArgumentException(ErrorMessages.ROLE_NOT_FOUND.getValue()));
                        roles.add(adminRole);
                    }
                    case "mod" -> {
                        Role modRole = roleRepository.findByName(RoleEnum.ROLE_MODERATOR)
                                .orElseThrow(() -> new IllegalArgumentException(ErrorMessages.ROLE_NOT_FOUND.getValue()));
                        roles.add(modRole);
                    }
                    default -> {
                        Role userRole = roleRepository.findByName(RoleEnum.ROLE_USER)
                                .orElseThrow(() -> new IllegalArgumentException(ErrorMessages.ROLE_NOT_FOUND.getValue()));
                        roles.add(userRole);
                    }
                }
            });
        }

        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roles)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        userRepository.save(user);
        return MessageResponse.builder()
                .message("User has been successfully registered!")
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
            )
        );
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new LocalAuthenticationException(ErrorMessages.USER_EMAIL_NOT_FOUND.getValue()));
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .tokenType("Bearer")
                .email(user.getEmail())
                .tokenExpiration(simpleDateFormat.format(new Date(System.currentTimeMillis() + 1000 * 60 * 24)))
                .roles(user
                        .getRoles()
                        .stream()
                        .map(Role::getName)
                        .toList())
                .build();
    }
}
