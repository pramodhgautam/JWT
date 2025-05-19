package com.nchl.JWT.dto;

import com.nchl.JWT.model.CreditorUserRoleMap;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.Set;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Firstname is required")
    private String firstName;

    @NotBlank(message = "Firstname is required")
    private String middleName;

    @NotBlank(message = "Lastname is required")
    private String lastName;

    @NotBlank(message = "Lastname is required")
    private String mobileNumber;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;

//    @NotBlank(message = "Outlet is required")
//    private String outlet;

    @NotBlank(message = "Terminal is required")
    private String terminal;

    @NotEmpty(message = "At least one role is required")
    private Set<Long> roleIds;
}