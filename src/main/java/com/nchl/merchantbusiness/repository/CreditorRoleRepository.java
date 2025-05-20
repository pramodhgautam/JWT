package com.nchl.merchantbusiness.repository;

import com.nchl.merchantbusiness.entity.CreditorRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditorRoleRepository extends JpaRepository<CreditorRole, Long>,
        JpaSpecificationExecutor<CreditorRole> {

}