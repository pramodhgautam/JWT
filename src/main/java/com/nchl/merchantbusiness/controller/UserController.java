package com.nchl.merchantbusiness.controller;

import com.nchl.merchantbusiness.constant.ResponseDetail;
import com.nchl.merchantbusiness.dto.APIResponse;
import com.nchl.merchantbusiness.entity.ChangePasswordRequest;
import com.nchl.merchantbusiness.entity.CreditorUser;
import com.nchl.merchantbusiness.exception.UserNotFoundException;
import com.nchl.merchantbusiness.repository.CreditorUserRepository;
import com.nchl.merchantbusiness.service.ChangePasswordService;
import com.nchl.merchantbusiness.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

private final ChangePasswordService changePasswordService;

    @PostMapping("/change-password")
    public ResponseEntity<APIResponse> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        return ResponseEntity.ok(changePasswordService.changePassword(request));
    }

}