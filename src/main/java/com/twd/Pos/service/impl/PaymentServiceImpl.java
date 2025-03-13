package com.twd.Pos.service.impl;

import com.twd.Pos.entity.Order;
import com.twd.Pos.entity.Payment;
import com.twd.Pos.repository.OrderRepository;
import com.twd.Pos.repository.PaymentRepository;
import com.twd.Pos.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Transactional
    @Override
    public Payment processPayment(Long orderId, BigDecimal amountPaid, String paymentMethod) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getPaymentStatus().equalsIgnoreCase("PAID")) {
            throw new RuntimeException("Order is already paid.");
        }

        if (amountPaid.compareTo(order.getTotal()) != 0) {
            throw new RuntimeException("Incorrect payment amount. Please pay the exact amount.");
        }

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmountPaid(amountPaid);
        payment.setPaymentMethod(paymentMethod);
        payment.setSuccessful(true);
        payment.setPaymentDate(LocalDateTime.now());

        order.setPaymentStatus("PAID");
        orderRepository.save(order);

        return paymentRepository.save(payment);
    }
}
