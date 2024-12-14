package com.twd.SpringSecurityJWT_Pos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.twd.SpringSecurityJWT_Pos.entity.Mail;

@Repository
public interface PasswordResetRequestRepository extends JpaRepository<Mail, Long> {

    Optional<Mail> findByToken(String token);

    Optional<Mail> findByEmail(String email);
    
}