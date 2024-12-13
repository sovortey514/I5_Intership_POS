package com.twd.SpringSecurityJWT_Pos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.twd.SpringSecurityJWT_Pos.entity.PasswordResetRequest;

@Repository
public interface PasswordResetRequestRepository extends JpaRepository<PasswordResetRequest, Long> {

    Optional<PasswordResetRequest> findByToken(String token);

    Optional<PasswordResetRequest> findByEmail(String email);
    
}