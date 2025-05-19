package com.nchl.JWT.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nchl.JWT.model.EntityAuditInfo;
import com.nchl.JWT.model.CreditorUserRoleMap;
import lombok.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreditorUserDto {

    @NotNull(message = "username must not be empty")
    @Size(min = 4, max = 50, message = "userName length should be Minimum 4 Maximum 50")
    private String username;

    private Long id;
    @Email
    private String email;
    @NotEmpty(message = "Enter Your Mobile Number")
    private String mobileNumber;
    private String firstName;
    private String middleName;
    private String lastName;
    private String passwordChangeStatus;
    private Set<CreditorUserRoleMap> creditorUserRoleMap;
    private String status;
    private boolean enable;
    private EntityAuditInfo entityAuditInfo;
    private String merchantName;
    private String subMerchantCode;
    private String legalName;
    private String refundEnabled;
    private String refundEnabledToUser;
    @NotEmpty
    private String merchantCode;
    @NotEmpty
    private Integer psSortCode;
    @NotEmpty
    private Integer roleId;

    private List<String> userRoles;
    private String userId;
    private String store;
    private String terminal;
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;

    private String role;
    private String outlet;

    private String userType;

    private String linkToken;
    private Date tokenExpiry;
    private String updatedDetails;
    private boolean accountSuspended;
    private Integer rowPerPage;

    private Integer page;

    private Long totalItems;

    private String searchUser;

    private Boolean firstLogin;

    private boolean voiceNotification;

    private String token;

    @Override
    public String toString() {
        return "{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", LastName='" + lastName + '\'' +
                ", passwordChangeStatus='" + passwordChangeStatus + '\'' +
                ", creditorUserRoleMap=" + creditorUserRoleMap +
                ", status='" + status + '\'' +
                ", enable=" + enable +
                ", entityAuditInfo=" + entityAuditInfo +
                ", merchantName='" + merchantName + '\'' +
                ", subMerchantCode='" + subMerchantCode + '\'' +
                ", merchantCode='" + merchantCode + '\'' +
                ", psSortCode=" + psSortCode +
                ", roleId=" + roleId +
                ", token='" + token + '\'' +
                '}';
    }
}



