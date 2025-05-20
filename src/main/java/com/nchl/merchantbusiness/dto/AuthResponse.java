package com.nchl.merchantbusiness.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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
    private List<Role> roles;
    private String id;
    private String jti;

    @Data
    @Builder
    public static class Role {
        private String authority;
    }

    @Data
    @Builder
    public static class TokenData {
        private String access_token;
        private String token_type;
        private String refresh_token;
        private Integer expires_in;
        private String scope;
    }

    @Builder.Default
    private boolean oneTime = true;
}