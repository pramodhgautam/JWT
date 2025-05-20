package com.nchl.merchantbusiness.service;

import com.nchl.merchantbusiness.dto.*;
import com.nchl.merchantbusiness.entity.CreditorRole;
import com.nchl.merchantbusiness.entity.CreditorUser;
import com.nchl.merchantbusiness.entity.CreditorUserRoleMap;
import com.nchl.merchantbusiness.constant.ResponseCode;
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

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private final CreditorRoleRepository creditorRoleRepository;

    public APIResponse<CreditorUserDto> register(RegisterRequest request) {
        try {

            if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                log.warn("Registration attempt with existing email: {}", request.getEmail());
                return (APIResponse<CreditorUserDto>) APIResponse.failure(
                        ResponseCode.CONFLICT,
                        "User with email " + request.getEmail() + " already exists"
                );
            }

            CreditorUser user = CreditorUser.builder()
                    .username(request.getUsername())
                    .firstName(request.getFirstName())
                    .middleName(request.getMiddleName())
                    .lastName(request.getLastName())
                    .mobileNumber(request.getMobileNumber())
                    .email(request.getEmail())
                    .terminal(request.getTerminal())
                    .build();

            CreditorRole userRole = null;
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
                                    .creditorUser(user)
                                    .build();
                        })
                        .collect(Collectors.toSet());

                user.setCreditorUserRoleMap(roleMappings);
            }

            CreditorUser savedUser = userRepository.save(user);
            log.info("New user registered with ID: {}", savedUser.getId());

            String jwtToken = jwtService.generateOneTimeToken(user);

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

            return APIResponse.success(
                    userData,
                    "User registered successfully"
            );

        } catch (Exception e) {
            log.error("Registration failed for email: {}", request.getEmail(), e);
            return (APIResponse<CreditorUserDto>) APIResponse.failure(
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

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        List<AuthResponse.Role> roles = user.getCreditorUserRoleMap().stream()
                .map(CreditorUserRoleMap::getCreditorRole)
                .flatMap(role -> {

                    Stream<AuthResponse.Role> roleStream = Stream.of(
                            AuthResponse.Role.builder()
                                    .authority(role.getName())
                                    .build()
                    );

                    if (role.getAllowedActionOrg() != null && !role.getAllowedActionOrg().isEmpty()) {
                        Stream<AuthResponse.Role> actionStream = role.getAllowedActionOrg().stream()
                                .map(action -> AuthResponse.Role.builder()
                                        .authority(action)
                                        .build());
                        return Stream.concat(roleStream, actionStream);
                    }
                    return roleStream;
                })
                .distinct()
                .collect(Collectors.toList());

        AuthResponse.TokenData tokenData = AuthResponse.TokenData.builder()
                .access_token(accessToken)
                .token_type("bearer")
                .refresh_token(refreshToken)
                .expires_in((int) (jwtService.getJwtExpiration() / 1000))
                .scope("read write trust")
                .build();

        return AuthResponse.builder()
                .code("000")
                .status("SUCCESS")
                .message("User authenticated successfully.")
                .data(tokenData)
                .roles(roles)
                .id(user.getUsername())
                .jti(UUID.randomUUID().toString())
                .build();
    }
}
