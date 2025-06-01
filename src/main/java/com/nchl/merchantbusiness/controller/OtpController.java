package com.nchl.merchantbusiness.controller;

import com.nchl.merchantbusiness.dto.OtpRequest;
import com.nchl.merchantbusiness.dto.OtpVerificationRequest;
import com.nchl.merchantbusiness.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/otp")
@RequiredArgsConstructor
public class OtpController {

    private final OtpService otpService;
    private final Map<String, String> otpStorage = new ConcurrentHashMap<>();

    @PostMapping("/send")
    public String sendOtp(@RequestBody OtpRequest request) {
        String otp = otpService.generateOtp();
        otpStorage.put(request.getEmail(), otp);
        otpService.sendOtpEmail(request.getEmail(), otp);
        return "OTP sent successfully to " + request.getEmail();
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpVerificationRequest request) {
        String storedOtp = otpStorage.get(request.getEmail());

        if (storedOtp == null) {
            return ResponseEntity.badRequest().body("No OTP found for this email");
        }

        if (!storedOtp.equals(request.getOtp())) {
            return ResponseEntity.badRequest().body("Invalid OTP");
        }

        otpStorage.remove(request.getEmail());
        return ResponseEntity.ok("OTP verified successfully");
    }

}