package com.twd.Pos.controller;

import com.twd.Pos.entity.Payment;
import com.twd.Pos.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/admin")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/process")
    public Payment processPayment(
            @RequestParam Long orderId,
            @RequestParam BigDecimal amountPaid,
            @RequestParam String paymentMethod) {
        return paymentService.processPayment(orderId, amountPaid, paymentMethod);
    }
}
