package com.nchl.merchantbusiness.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChangePasswordRequest {
    private String username;
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;

    public ChangePasswordRequest() {
    }

    public ChangePasswordRequest(String username, String oldPassword, String newPassword, String confirmPassword) {
        this.username = username;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }

}