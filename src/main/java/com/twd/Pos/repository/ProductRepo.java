package com.twd.Pos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.twd.Pos.entity.Product;

public interface ProductRepo extends JpaRepository<Product, Integer> {
}
