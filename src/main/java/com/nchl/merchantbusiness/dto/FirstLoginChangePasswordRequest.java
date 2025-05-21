package com.nchl.merchantbusiness.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FirstLoginChangePasswordRequest {

    private String username;
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;

}