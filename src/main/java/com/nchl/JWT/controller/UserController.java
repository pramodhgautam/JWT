package com.nchl.JWT.controller;

import com.nchl.JWT.dto.ApiResponse;
import com.nchl.JWT.dto.UserDto;
import com.nchl.JWT.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<UserDto>>> getAllUsers() {
        ApiResponse<List<UserDto>> response = userService.getAllUsers();
        return ResponseEntity
                .status(getHttpStatusFromResponseCode(response.getResponseCode()))
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDto>> getUserById(@PathVariable Integer id) {
        log.debug("Fetching user with ID: {}", id);
        ApiResponse<UserDto> response = userService.getUserById(id);
        log.debug("Found user response: {}", response);
        return ResponseEntity
                .status(getHttpStatusFromResponseCode(response.getResponseCode()))
                .body(response);
    }

    // Helper method to map response codes to HTTP statuses
    private HttpStatus getHttpStatusFromResponseCode(String responseCode) {
        return switch (responseCode) {
            case "000" -> HttpStatus.OK;
            case "404" -> HttpStatus.NOT_FOUND;
            case "401" -> HttpStatus.UNAUTHORIZED;
            case "409" -> HttpStatus.CONFLICT;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }
}