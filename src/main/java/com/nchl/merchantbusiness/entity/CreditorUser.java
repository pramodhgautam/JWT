package com.nchl.merchantbusiness.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "creditor_user")
@SequenceGenerator(name="creditor_user_id_seq", sequenceName = "creditor_user_id_seq", allocationSize=1)
public class CreditorUser implements UserDetails {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "creditor_user_id_seq")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "mobile_number")
    private String mobileNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "ps_short_code")
    private Integer psSortCode;

    @Column(name = "merchant_code")
    private String merchantCode;

    @Column(name = "sub_merchant_code")
    private String subMerchantCode;

    @Column(name="first_login_flag")
    private boolean firstLogin;

    @Column(name="no_of_login_attempts")
    private Integer loginAttempt;

    @Column(name = "is_enable")
    private boolean isEnable;

    @Column(name="device_id")
    private String deviceId;

    @Column(name = "pwd_change_status")
    private String passwordChangeStatus;

    @Column(name="notification_id")
    private String notificationId;

    @Column(name = "status")
    private  String status;

    @Embedded
    private EntityAuditInfo entityAuditInfo;

    @OneToMany(mappedBy = "creditorUser", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnoreProperties("creditorUser")
    private Set<CreditorUserRoleMap> creditorUserRoleMap;

    @Column(name = "last_wrong_entry")
    private Date lastWrongEntryDate;

    @Column(name = "account_locked")
    private boolean accountLocked;

    @Column(name = "account_suspended")
    private boolean accountSuspended;

    @Column(name="legal_name")
    private String legalName;

    @Column(name="terminal")
    private String terminal;

    @Column(name = "pwd_lchg_date")
    private LocalDateTime passwordLastChangeDate;

    @Column(name = "user_registered_by")
    private String userRegisteredThrough;

    @Column(name = "voice_notification")
    private boolean voiceNotification;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.getCreditorUserRoleMap().stream()
                .map(CreditorUserRoleMap::getCreditorRole)
                .filter(role -> role.getAllowedActionOrg() != null)
                .flatMap(role -> role.getAllowedActionOrg().stream())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Modify as needed
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Modify as needed
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Modify as needed
    }

    @Override
    public boolean isEnabled() {
        return true; // Modify as needed
    }
}