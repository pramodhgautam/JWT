package com.nchl.JWT.repository;

import com.nchl.JWT.model.CreditorUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<CreditorUser, Integer> {
    Optional<CreditorUser> findByEmail(String email);
}