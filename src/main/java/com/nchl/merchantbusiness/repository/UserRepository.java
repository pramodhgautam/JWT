package com.nchl.merchantbusiness.repository;

import com.nchl.merchantbusiness.model.CreditorUser;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<CreditorUser, Long> {
    Optional<CreditorUser> findByEmail(String email);
    Optional<CreditorUser> findByUsername(String username); // Add this method
    Optional<CreditorUser> findById(Integer id);

}