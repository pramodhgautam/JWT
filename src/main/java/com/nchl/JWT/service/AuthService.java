package com.nchl.JWT.service;

import com.nchl.JWT.dto.*;
import com.nchl.JWT.exception.UserAlreadyExistsException;
import com.nchl.JWT.model.Role;
import com.nchl.JWT.model.CreditorUser;
import com.nchl.JWT.repository.UserRepository;
import com.nchl.JWT.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public ApiResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User with email " + request.getEmail() + " already exists");
        }

        var user = CreditorUser.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole() != null ? request.getRole() : Role.USER)
                .build();

        CreditorUser savedUser=userRepository.save(user);

        var jwtToken = jwtService.generateToken((CreditorUser) user);

        UserResponse userData = UserResponse.builder()
                .userId(savedUser.getId())
                .email(savedUser.getEmail())
                .role(savedUser.getRole().name())
                .firstname(savedUser.getFirstname())
                .lastname(savedUser.getLastname())
                .createdAt(LocalDateTime.now())
                .build();

        return ApiResponse.builder()
                .responseCode("000")
                .responseMessage("User registered successfully.")
                .data(userData)
                .build();
    }

    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken((CreditorUser) user);
        return AuthResponse.builder()
                .responseCode("000")
                .responseMessage("User authenticated successfully.")
                .token(jwtToken)
                .expiresAt(LocalDateTime.now().plusMinutes(30))
                .build();
    }
}