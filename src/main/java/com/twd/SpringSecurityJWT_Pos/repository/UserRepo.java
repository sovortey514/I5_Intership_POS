package com.twd.SpringSecurityJWT_Pos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.twd.SpringSecurityJWT_Pos.entity.User;

import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
}
