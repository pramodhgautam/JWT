package com.nchl.JWT.service;

import com.nchl.JWT.dto.*;
import com.nchl.JWT.exception.UserAlreadyExistsException;
import com.nchl.JWT.model.ResponseCode;
import com.nchl.JWT.model.Role;
import com.nchl.JWT.model.CreditorUser;
import com.nchl.JWT.repository.UserRepository;
import com.nchl.JWT.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public ApiResponse<UserResponse> register(RegisterRequest request) {
        try {
            // Check if user already exists
            if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                log.warn("Registration attempt with existing email: {}", request.getEmail());
                return (ApiResponse<UserResponse>) ApiResponse.failure(
                        ResponseCode.CONFLICT,
                        "User with email " + request.getEmail() + " already exists"
                );
            }

            // Build and save new user
            CreditorUser user = CreditorUser.builder()
                    .firstname(request.getFirstname())
                    .lastname(request.getLastname())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(request.getRole() != null ? request.getRole() : Role.USER)
                    .build();

            CreditorUser savedUser = userRepository.save(user);
            log.info("New user registered with ID: {}", savedUser.getId());

            // Generate JWT token (if needed for immediate login)
            String jwtToken = jwtService.generateToken(user);

            // Build response data
            UserResponse userData = UserResponse.builder()
                    .userId(savedUser.getId())
                    .email(savedUser.getEmail())
                    .role(savedUser.getRole().name())
                    .firstname(savedUser.getFirstname())
                    .lastname(savedUser.getLastname())
                    .createdAt(LocalDateTime.now())
                    .token(jwtToken)
                    .build();

            // Return success response
            return ApiResponse.success(
                    userData,
                    "User registered successfully"
            );

        } catch (Exception e) {
            log.error("Registration failed for email: {}", request.getEmail(), e);
            return (ApiResponse<UserResponse>) ApiResponse.failure(
                    ResponseCode.SERVER_ERROR,
                    "Registration failed. Please try again later."
            );
        }
    }

    public AuthResponse authenticate(AuthRequest request) {

        CreditorUser user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        request.getPassword()
                )
        );

        // Generate token with ID as subject
        String jwtToken = jwtService.generateOneTimeToken(user);

        return AuthResponse.builder()
                .responseCode("000")
                .responseMessage("User authenticated successfully.")
                .token(jwtToken)
                .build();
    }
}