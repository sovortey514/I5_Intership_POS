package com.twd.Pos.service;

import java.math.BigDecimal;

import com.twd.Pos.entity.Payment;

public interface PaymentService {
    Payment processPayment(Long orderId, BigDecimal amountPaid, String paymentMethod);
}
