package com.nchl.merchantbusiness.controller;

import com.nchl.merchantbusiness.constant.ResponseDetail;
import com.nchl.merchantbusiness.dto.APIResponse;
import com.nchl.merchantbusiness.dto.AuthRequest;
import com.nchl.merchantbusiness.dto.RefreshTokenRequest;
import com.nchl.merchantbusiness.dto.RegisterRequest;
import com.nchl.merchantbusiness.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<APIResponse> getAccessToken(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public APIResponse refreshToken(@Valid @RequestBody RefreshTokenRequest request) {

        return authService.refreshToken(request);
    }

}
