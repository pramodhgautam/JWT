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
    private String role;  // Single role name
    private List<String> authorities;  // List of action authorities
    private String id;
    private String jti;
    private boolean firstLogin;

    @Data
    @Builder
    public static class TokenData {
        private String access_token;
        private String token_type;
        private String refresh_token;
        private Integer expires_in;
        private String scope;
    }
}