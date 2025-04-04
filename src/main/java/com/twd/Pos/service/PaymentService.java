package com.twd.Pos.service;

import java.math.BigDecimal;
import java.util.List;

import com.twd.Pos.dto.PaymentOrderDTO;
import com.twd.Pos.entity.Payment;

public interface PaymentService {
    Payment processPayment(Long orderId, BigDecimal amountPaid, String paymentMethod,String currency);

    Payment processPaymentWithMembership(Long orderId, BigDecimal amountPaid, String paymentMethod, String membershipId);

    List<PaymentOrderDTO> getAllPaymentsWithOrderDetails();

    PaymentOrderDTO getPaymentById(Long paymentId);

}
