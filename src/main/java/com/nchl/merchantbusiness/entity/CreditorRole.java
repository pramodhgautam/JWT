package com.nchl.merchantbusiness.entity;

import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;

import jakarta.persistence.*;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "creditor_role")
public class CreditorRole implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "is_enable")
    private boolean enable;

    @Column(name="allowed_action")
    private String allowedAction;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "allowed_action_org",columnDefinition = "jsonb")
    private Set<String> allowedActionOrg;

}
