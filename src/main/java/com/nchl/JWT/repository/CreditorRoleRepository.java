package com.nchl.JWT.repository;

import com.nchl.JWT.model.CreditorRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface CreditorRoleRepository extends JpaRepository<CreditorRole, Long>,
        JpaSpecificationExecutor<CreditorRole> {

}