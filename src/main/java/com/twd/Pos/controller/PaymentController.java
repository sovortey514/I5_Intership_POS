package com.twd.Pos.controller;

import com.twd.Pos.dto.PaymentRequest;
import com.twd.Pos.entity.Payment;
import com.twd.Pos.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/auth")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/process")
    public Payment processPayment(@RequestBody PaymentRequest request) {
        return paymentService.processPayment(request.getOrderId(), request.getAmountPaid(), request.getPaymentMethod());
    }

    @PostMapping("/processWithMembership")
    public Payment processPaymentWithMembership(@RequestBody PaymentRequest request) {
        return paymentService.processPaymentWithMembership(request.getOrderId(), request.getAmountPaid(), request.getPaymentMethod(), request.getMembershipId());
    }
}
