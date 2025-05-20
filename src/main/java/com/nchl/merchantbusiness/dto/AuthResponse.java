package com.nchl.merchantbusiness.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse <T> {
    private String responseCode;
    private String responseMessage;
    private String token;
    @Builder.Default
    private boolean oneTime = true; // Default to one-time tokens

}