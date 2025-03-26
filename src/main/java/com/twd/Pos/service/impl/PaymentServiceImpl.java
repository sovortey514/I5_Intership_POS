package com.twd.Pos.service.impl;

import com.twd.Pos.dto.PaymentOrderDTO;
import com.twd.Pos.entity.Membership;
import com.twd.Pos.entity.Order;
import com.twd.Pos.entity.Payment;
import com.twd.Pos.repository.MembershipRepository;
import com.twd.Pos.repository.OrderRepository;
import com.twd.Pos.repository.PaymentRepository;
import com.twd.Pos.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MembershipRepository membershipRepository;

    @Transactional
    @Override
    public Payment processPayment(Long orderId, BigDecimal amountPaid, String paymentMethod) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    
        if (order.getPaymentStatus().equalsIgnoreCase("PAID")) {
            throw new RuntimeException("Order is already paid.");
        }
    
        BigDecimal totalAmount = order.getTotal();
        
        if (amountPaid.compareTo(totalAmount) < 0) {
            throw new RuntimeException("Insufficient payment. Please pay the full amount.");
        }
    
        BigDecimal cashBack = BigDecimal.ZERO;
        if (amountPaid.compareTo(totalAmount) > 0) {
            cashBack = amountPaid.subtract(totalAmount);
        }
    
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmountPaid(amountPaid);
        payment.setPaymentMethod(paymentMethod);
        payment.setStatus("PAID");
        payment.setSuccessful(true);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setCashBack(cashBack); 
    
        order.setPaymentStatus("PAID");
        orderRepository.save(order);
    
        return paymentRepository.save(payment);
    }
    
    @Override
    @Transactional
    public Payment processPaymentWithMembership(Long orderId, BigDecimal amountPaid, String paymentMethod,
            String membershipId) {
        
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Membership membership = membershipRepository.findByMembershipId(membershipId)
                .orElseThrow(() -> new RuntimeException("Membership card not found"));

        if (membership.getBalance() < amountPaid.doubleValue()) {
            throw new RuntimeException("Insufficient balance on membership card");
        }

        membership.setBalance(membership.getBalance() - amountPaid.doubleValue());
        membershipRepository.save(membership); 

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmountPaid(amountPaid);
        payment.setPaymentMethod(paymentMethod); 
        payment.setStatus("PAID");
        payment.setPaymentDate(LocalDateTime.now()); 
        order.setPaymentStatus("PAID");
        payment = paymentRepository.save(payment); 
        orderRepository.save(order); 

        return payment; 
    }

    @Override
    public List<PaymentOrderDTO> getAllPaymentsWithOrderDetails() {
        return paymentRepository.findPaymentsWithOrderDetails(); // Fetch payments with their order details
    }

    @Override
    @Transactional
    public PaymentOrderDTO getPaymentById(Long paymentId) {
        // Fetch payment by ID
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        // Fetch associated order
        Order order = payment.getOrder();

        Long tableId = null;
        if (order.getTable() != null) {
            tableId = order.getTable().getId();  // Get tableId from the Table entity
        }

        // Create and populate PaymentOrderDTO with the necessary data
        PaymentOrderDTO paymentOrderDTO = new PaymentOrderDTO(
            payment.getId(),
            payment.getPaymentMethod(),
            payment.getAmountPaid(),
            payment.getPaymentDate(),
            order.getCustomOrderId(),  // Assuming CustomOrderId is the ID you need
            order.getTotal(),  // Total amount of the order
            payment.getMembership() != null ? payment.getMembership().getId() : null  // If applicable, Membership ID
        );

        // Add additional fields like cashback, order status, and tableId
        paymentOrderDTO.setCashBack(payment.getCashBack());
        paymentOrderDTO.setOrderStatus(order.getPaymentStatus());  // Order status (PAID, PENDING, etc.)
        paymentOrderDTO.setTableId(tableId);  // Assuming the table ID is part of the order

        return paymentOrderDTO;
    }

}
