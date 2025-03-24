package com.twd.Pos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.twd.Pos.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long>{
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.payment WHERE o.paymentStatus = :paymentStatus")
    List<Order> findAllOrdersWithPayment(@Param("paymentStatus") String paymentStatus);
}
