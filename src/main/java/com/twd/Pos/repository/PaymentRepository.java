package com.twd.Pos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.twd.Pos.entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>{
    
}
