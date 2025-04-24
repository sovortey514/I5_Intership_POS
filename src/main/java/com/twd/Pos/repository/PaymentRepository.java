package com.twd.Pos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.twd.Pos.dto.PaymentOrderDTO;
import com.twd.Pos.entity.Order;
import com.twd.Pos.entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
  
    @Query("SELECT new com.twd.Pos.dto.PaymentOrderDTO(p.id, p.paymentMethod, p.amountPaid, p.paymentDate, o.customOrderId, o.total, "
    + "m.id, m.membershipType, m.name, m.gender, m.balance, p.cashBack) "
    + "FROM Payment p "
    + "LEFT JOIN p.order o "
    + "LEFT JOIN p.membership m "
    + "WHERE p.isSuccessful = true")
    List<PaymentOrderDTO> findPaymentsWithOrderDetails();

}
