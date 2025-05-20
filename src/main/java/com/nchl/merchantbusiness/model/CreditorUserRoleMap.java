package com.nchl.merchantbusiness.model;

import lombok.*;

import jakarta.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "creditor_user_role_map")
public class CreditorUserRoleMap implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    private CreditorRole creditorRole;
//    @OneToMany(fetch = FetchType.LAZY)
//    @JoinColumn(name = "role_id")
//    private Set<CreditorRole> creditorRole;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creditor_user_id")
    private CreditorUser creditorUser;



}
