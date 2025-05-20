package com.nchl.merchantbusiness.service;

import com.nchl.merchantbusiness.dto.*;
import com.nchl.merchantbusiness.model.CreditorRole;
import com.nchl.merchantbusiness.model.CreditorUser;
import com.nchl.merchantbusiness.model.CreditorUserRoleMap;
import com.nchl.merchantbusiness.model.ResponseCode;
import com.nchl.merchantbusiness.repository.CreditorRoleRepository;
import com.nchl.merchantbusiness.repository.UserRepository;
import com.nchl.merchantbusiness.repository.specification.CreditorRoleSpecification;
import com.nchl.merchantbusiness.security.JwtService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private final CreditorRoleRepository creditorRoleRepository;

    public ApiResponse<CreditorUserDto> register(RegisterRequest request) {
        try {
            // Check if user already exists
            if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                log.warn("Registration attempt with existing email: {}", request.getEmail());
                return (ApiResponse<CreditorUserDto>) ApiResponse.failure(
                        ResponseCode.CONFLICT,
                        "User with email " + request.getEmail() + " already exists"
                );
            }

            // Build and save new user
            CreditorUser user = CreditorUser.builder()
                    .username(request.getUsername())
                    .firstName(request.getFirstName())
                    .middleName(request.getMiddleName())
                    .lastName(request.getLastName())
                    .mobileNumber(request.getMobileNumber())
                    .email(request.getEmail())
                    .terminal(request.getTerminal())
                    .build();

            // Handle role mapping
            CreditorRole userRole = null; // We'll store the first role here
            if (request.getRoleIds() != null && !request.getRoleIds().isEmpty()) {
                Set<CreditorUserRoleMap> roleMappings = request.getRoleIds().stream()
                        .map(roleId -> {

                            Specification<CreditorRole> creditorUserSpecification = CreditorRoleSpecification
                                    .byId(roleId);

                            CreditorRole role = creditorRoleRepository
                                    .findOne(creditorUserSpecification)
                                    .orElseThrow(() -> new EntityNotFoundException("Role not found with id: " + roleId));

                            return CreditorUserRoleMap.builder()
                                    .creditorRole(role)
                                    .creditorUser(user) // Set the bidirectional relationship
                                    .build();
                        })
                        .collect(Collectors.toSet());

                user.setCreditorUserRoleMap(roleMappings);
            }

            CreditorUser savedUser = userRepository.save(user);
            log.info("New user registered with ID: {}", savedUser.getId());

            // Generate JWT token (if needed for immediate login)
            String jwtToken = jwtService.generateOneTimeToken(user);

            // Build response data
            CreditorUserDto userData = CreditorUserDto.builder()
                    .id(savedUser.getId())
                    .username(savedUser.getUsername())
                    .firstName(savedUser.getFirstName())
                    .middleName(savedUser.getMiddleName())
                    .lastName(savedUser.getLastName())
                    .mobileNumber(savedUser.getMobileNumber())
                    .email(savedUser.getEmail())
                    .terminal(savedUser.getTerminal())
                    .role(String.valueOf(userRole != null ? userRole.getId().intValue() : null))
                    .token(jwtToken)
                    .build();

            // Return success response
            return ApiResponse.success(
                    userData,
                    "User registered successfully"
            );

        } catch (Exception e) {
            log.error("Registration failed for email: {}", request.getEmail(), e);
            return (ApiResponse<CreditorUserDto>) ApiResponse.failure(
                    ResponseCode.SERVER_ERROR,
                    "Registration failed. Please try again later."
            );
        }
    }

    public AuthResponse<Object> authenticate(@RequestBody AuthRequest request) {

        CreditorUser user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        request.getPassword()
                )
        );

        // Generate token with ID as subject
        String jwtToken = jwtService.generateToken(user);

        return AuthResponse.builder()
                .responseCode("000")
                .responseMessage("User authenticated successfully.")
                .token(jwtToken)
                .build();
    }
}