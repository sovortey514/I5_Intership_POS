package com.twd.SpringSecurityJWT_Pos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.twd.SpringSecurityJWT_Pos.entity.Product;

public interface ProductRepo extends JpaRepository<Product, Integer> {
}
