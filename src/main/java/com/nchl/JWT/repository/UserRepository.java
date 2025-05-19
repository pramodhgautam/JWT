package com.nchl.JWT.repository;

import com.nchl.JWT.model.CreditorUser;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<CreditorUser, Long> {
    Optional<CreditorUser> findByEmail(String email);
    Optional<CreditorUser> findById(Integer id);

}