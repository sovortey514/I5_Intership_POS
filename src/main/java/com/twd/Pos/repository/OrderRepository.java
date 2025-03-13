package com.twd.Pos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.twd.Pos.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long>{
    
}
