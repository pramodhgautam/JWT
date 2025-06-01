package com.nchl.merchantbusiness.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse<T> {
    private String code;
    private String status;
    private String message;
    private TokenData data;
    private String role;
    private List<String> authorities;
    private String id;
    private String jti;
    private boolean firstLogin;

    @Data
    @Builder
    public static class TokenData {
        private String accessToken;
        private String tokenType;
        private String refreshToken;
        private Integer expiresIn;
        private String scope;
    }
}