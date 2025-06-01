package com.nchl.merchantbusiness.repository;

import java.util.Optional;

import com.nchl.merchantbusiness.entity.CreditorUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditorUserRepository extends JpaRepository<CreditorUser, Long> {
    Optional<CreditorUser> findByUsername(String username);
}
