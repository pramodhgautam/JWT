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
public class UserResponse {
    private Integer userId;
    private String email;
    private String role;
    private String firstname;
    private String lastname;
    private LocalDateTime createdAt;
    private String token;
}

