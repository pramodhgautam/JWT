package com.nchl.merchantbusiness.service;

import com.nchl.merchantbusiness.constant.ResponseDetail;
import com.nchl.merchantbusiness.dto.*;
import com.nchl.merchantbusiness.dto.response.TokenData;
import com.nchl.merchantbusiness.entity.CreditorRole;
import com.nchl.merchantbusiness.entity.CreditorUser;
import com.nchl.merchantbusiness.entity.CreditorUserRoleMap;
import com.nchl.merchantbusiness.repository.CreditorRoleRepository;
import com.nchl.merchantbusiness.repository.UserRepository;
import com.nchl.merchantbusiness.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private final CreditorRoleRepository creditorRoleRepository;

    public APIResponse authenticate(@RequestBody AuthRequest request) {

        try {

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

            String roleName = user.getCreditorUserRoleMap().stream()
                    .findFirst()
                    .map(CreditorUserRoleMap::getCreditorRole)
                    .map(CreditorRole::getName)
                    .orElse("DEFAULT_ROLE");

            List<String> authorities = user.getCreditorUserRoleMap().stream()
                    .map(CreditorUserRoleMap::getCreditorRole)
                    .filter(role -> role.getAllowedActionOrg() != null)
                    .flatMap(role -> role.getAllowedActionOrg().stream())
                    .collect(Collectors.toList());

            TokenData tokenData = TokenData.builder()
                    .accessToken(accessToken)
                    .tokenType("Bearer")
                    .refreshToken(refreshToken)
                    .expiresIn((int) (jwtService.getJwtExpiration() / 1000))
                    .build();

            return APIResponse.apiResponse(ResponseDetail.SUCCESS, "User authenticated successfully.",tokenData);

        } catch (Exception e) {
            log.error("signin error: ", e);
            return APIResponse.apiResponse(ResponseDetail.FAILURE,"Failed to get token.",Collections.emptyMap());
        }
    }

    public APIResponse refreshToken(@RequestBody RefreshTokenRequest request) {
        try {
            String refreshToken = request.getRefreshToken();

            String username = jwtService.extractUsername(refreshToken);

            CreditorUser user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new BadCredentialsException("Invalid refresh token"));

            if (!jwtService.isRefreshTokenValid(refreshToken, user)) {
                throw new BadCredentialsException("Invalid or expired refresh token");
            }

            TokenData tokenData= generateTokenResponse(user);

            return APIResponse.apiResponse(ResponseDetail.SUCCESS,"SUCCESS", tokenData);
        } catch (Exception e) {
           return APIResponse.apiResponse(ResponseDetail.FAILURE,"Failed to get token.",Collections.emptyMap());
        }
    }

    private TokenData generateTokenResponse(CreditorUser user) {

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        String roleName = user.getCreditorUserRoleMap().stream()
                .findFirst()
                .map(CreditorUserRoleMap::getCreditorRole)
                .map(CreditorRole::getName)
                .orElse("DEFAULT_ROLE");

        List<String> authorities = user.getCreditorUserRoleMap().stream()
                .map(CreditorUserRoleMap::getCreditorRole)
                .filter(role -> role.getAllowedActionOrg() != null)
                .flatMap(role -> role.getAllowedActionOrg().stream())
                .collect(Collectors.toList());

        TokenData tokenData = TokenData.builder()
                .accessToken(accessToken)
                .tokenType("bearer")
                .refreshToken(refreshToken)
                .expiresIn((int) (jwtService.getJwtExpiration() / 1000))
                .build();

        return tokenData;

    }
}
