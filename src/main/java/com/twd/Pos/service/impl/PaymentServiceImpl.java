package com.twd.Pos.service.impl;

import com.twd.Pos.dto.PaymentOrderDTO;
import com.twd.Pos.entity.Membership;
import com.twd.Pos.entity.Order;
import com.twd.Pos.entity.OrderItem;
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

        BigDecimal totalAmount = order.getOrderItems().stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal taxAmount = totalAmount.multiply(new BigDecimal("0.05"));

        BigDecimal totalAmountWithTax = totalAmount.add(taxAmount);

        System.out.println("Total amount (including tax): " + totalAmountWithTax);

        if (amountPaid.compareTo(totalAmountWithTax) < 0) {
            throw new RuntimeException("Insufficient payment. Please pay the full amount.");
        }

        BigDecimal cashBack = BigDecimal.ZERO;
        if (amountPaid.compareTo(totalAmountWithTax) > 0) {
            cashBack = amountPaid.subtract(totalAmountWithTax);
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

    // @Override
    // @Transactional
    // public Payment processPaymentWithMembership(Long orderId, BigDecimal amountPaid, String paymentMethod,
    //         String membershipId) {

    //     Order order = orderRepository.findById(orderId)
    //             .orElseThrow(() -> new RuntimeException("Order not found"));

    //     Membership membership = membershipRepository.findByMembershipId(membershipId)
    //             .orElseThrow(() -> new RuntimeException("Membership card not found"));

    //     if (membership.getBalance() < amountPaid.doubleValue()) {
    //         throw new RuntimeException("Insufficient balance on membership card");
    //     }

    //     membership.setBalance(membership.getBalance() - amountPaid.doubleValue());
    //     membershipRepository.save(membership);

    //     Payment payment = new Payment();
    //     payment.setOrder(order);
    //     payment.setAmountPaid(amountPaid);
    //     payment.setPaymentMethod(paymentMethod);
    //     payment.setStatus("PAID");
    //     payment.setPaymentDate(LocalDateTime.now());
    //     order.setPaymentStatus("PAID");
    //     payment = paymentRepository.save(payment);
    //     orderRepository.save(order);

    //     return payment;
    // }
    @Override
    @Transactional
    public Payment processPaymentWithMembership(Long orderId, BigDecimal amountPaid, String paymentMethod,
            String membershipId) {
    
        // Retrieve the order and membership details
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    
        Membership membership = membershipRepository.findByMembershipId(membershipId)
                .orElseThrow(() -> new RuntimeException("Membership card not found"));
    
        // Show the current balance of the membership
        System.out.println("Current membership balance: " + membership.getBalance());
    
        // Check if the membership balance is sufficient
        if (membership.getBalance() < amountPaid.doubleValue()) {
            throw new RuntimeException("Insufficient balance on membership card");
        }
    
        // Calculate tax (5% of the amountPaid)
        BigDecimal taxAmount = amountPaid.multiply(BigDecimal.valueOf(0.05));
        BigDecimal totalAmount = amountPaid.add(taxAmount); // Total amount including tax
    
        // Update the membership balance
        membership.setBalance(membership.getBalance() - totalAmount.doubleValue());
    
        // Show the new balance after payment processing
        System.out.println("Updated membership balance after payment: " + membership.getBalance());
    
        membershipRepository.save(membership);
    
        // Create the payment object with the updated amount
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmountPaid(totalAmount); 
        payment.setPaymentMethod(paymentMethod);
        payment.setStatus("PAID");
        payment.setPaymentDate(LocalDateTime.now());
        order.setPaymentStatus("PAID");
        membership.setBalance(0);
    
        // Save the payment and order
        payment = paymentRepository.save(payment);
        orderRepository.save(order);
    
        return payment;
    }
    


    @Override
    public List<PaymentOrderDTO> getAllPaymentsWithOrderDetails() {
        return paymentRepository.findPaymentsWithOrderDetails();
    }

    @Override
    @Transactional
    public PaymentOrderDTO getPaymentById(Long paymentId) {

    Payment payment = paymentRepository.findById(paymentId)
    .orElseThrow(() -> new RuntimeException("Payment not found"));

    Order order = payment.getOrder();

    Long tableId = null;
    if (order.getTable() != null) {
    tableId = order.getTable().getId();
    }

    PaymentOrderDTO paymentOrderDTO = new PaymentOrderDTO(
    payment.getId(),
    payment.getPaymentMethod(),
    payment.getAmountPaid(),
    payment.getPaymentDate(),
    order.getCustomOrderId(),
    order.getTotal(),
    payment.getMembership() != null ? payment.getMembership().getId() : null
    );

    paymentOrderDTO.setCashBack(payment.getCashBack());
    paymentOrderDTO.setOrderStatus(order.getPaymentStatus());
    paymentOrderDTO.setTableId(tableId);

    return paymentOrderDTO;
    }

    

}
