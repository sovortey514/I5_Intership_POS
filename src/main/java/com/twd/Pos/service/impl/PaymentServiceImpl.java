package com.twd.Pos.service.impl;

import com.twd.Pos.dto.PaymentOrderDTO;
import com.twd.Pos.entity.Bakong;
import com.twd.Pos.entity.Membership;
import com.twd.Pos.entity.Order;
import com.twd.Pos.entity.OrderItem;
import com.twd.Pos.entity.Payment;
import com.twd.Pos.repository.BakongRepository;
import com.twd.Pos.repository.MembershipRepository;
import com.twd.Pos.repository.OrderRepository;
import com.twd.Pos.repository.PaymentRepository;
import com.twd.Pos.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.math.RoundingMode;
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

    @Autowired
    private BakongRepository bakongRepository;

    @Transactional
    @Override
    public Payment processPayment(Long orderId, BigDecimal amountPaid, String paymentMethod, String currency) {
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

        if ("KHR".equalsIgnoreCase(currency)) {
            totalAmountWithTax = totalAmountWithTax.multiply(new BigDecimal("4100"));
            System.out.println("Total amount (including tax) in KHR: " + totalAmountWithTax);
        }

        BigDecimal finalAmount = totalAmountWithTax;

        if ("KHR".equalsIgnoreCase(currency)) {
            amountPaid = amountPaid;
            finalAmount = totalAmountWithTax;
        } else {

            if ("USD".equalsIgnoreCase(currency) || "USDT".equalsIgnoreCase(currency)) {

                finalAmount = finalAmount;
            }
        }

        if (amountPaid.compareTo(finalAmount) < 0) {
            throw new RuntimeException("Insufficient payment. Please pay the full amount.");
        }

        BigDecimal cashBack = amountPaid.subtract(finalAmount);

        System.out.println("cashBack in " + currency + ": " + cashBack);

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
    public List<PaymentOrderDTO> getAllPaymentsWithOrderDetails() {
        return paymentRepository.findPaymentsWithOrderDetails();
    }

    @Transactional
    @Override
    public PaymentOrderDTO getPaymentById(Long paymentId) {
        // Fetch the payment by ID
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        // Fetch associated order
        Order order = payment.getOrder();

        // Fetch associated membership
        Membership membership = payment.getMembership();

        // Map the payment and membership details to PaymentOrderDTO
        return new PaymentOrderDTO(
                payment.getId(),
                payment.getPaymentMethod(),
                payment.getAmountPaid(),
                payment.getPaymentDate(),
                order.getCustomOrderId(),
                order.getTotal(),
                membership != null ? membership.getId() : null,
                membership != null ? membership.getMembershipType() : null,
                membership != null ? membership.getName() : null,
                membership != null ? membership.getGender() : null,
                membership != null ? membership.getBalance() : null,
                payment.getCashBack());
    }

    @Override
    @Transactional
    public PaymentOrderDTO processPaymentWithMembershipDTO(Long orderId, BigDecimal amountPaid, String paymentMethod,
            Long membershipId) {

        // Fetch the order associated with the payment
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Fetch the membership associated with the payment
        Membership membership = membershipRepository.findById(membershipId)
                .orElseThrow(() -> new RuntimeException("Membership card not found"));

        System.out.println("Current membership balance: " + membership.getBalance());

        // Check if the membership balance is sufficient
        if (membership.getBalance() < amountPaid.doubleValue()) {
            throw new RuntimeException("Insufficient balance on membership card");
        }

        // Calculate tax and total amount
        BigDecimal taxAmount = amountPaid.multiply(BigDecimal.valueOf(0.05));
        BigDecimal totalAmount = amountPaid.add(taxAmount);

        // Update the membership balance
        membership.setBalance(membership.getBalance() - totalAmount.doubleValue());
        membershipRepository.save(membership); // Save the updated membership

        System.out.println("Updated membership balance after payment: " + membership.getBalance());

        // Create a payment object
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmountPaid(totalAmount);
        payment.setPaymentMethod(paymentMethod);
        payment.setStatus("PAID");
        payment.setPaymentDate(LocalDateTime.now());
        payment.setMembership(membership); // Link the membership to the payment
        payment.setCashBack(BigDecimal.ZERO); 

        // Save the payment object
        payment = paymentRepository.save(payment);
        order.setPaymentStatus("PAID");
        orderRepository.save(order);


        return new PaymentOrderDTO(
                payment.getId(),
                payment.getPaymentMethod(),
                payment.getAmountPaid(),
                payment.getPaymentDate(),
                order.getCustomOrderId(),
                order.getTotal(),
                membership.getId(),
                membership.getMembershipType(),
                membership.getName(),
                membership.getGender(),
                membership.getBalance(),
                payment.getCashBack());
    }

    @Transactional
    @Override
    public Payment processPaymentwithbakong(Long orderId, BigDecimal amountPaid, String paymentMethod,
            String currency) {
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

        if ("KHR".equalsIgnoreCase(currency)) {
            totalAmountWithTax = totalAmountWithTax.multiply(new BigDecimal("4100"));
            System.out.println("Total amount (including tax) in KHR: " + totalAmountWithTax);
        }

        BigDecimal finalAmount = totalAmountWithTax;

        if (amountPaid.compareTo(finalAmount) < 0) {
            throw new RuntimeException("Insufficient payment. Please pay the full amount.");
        }

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmountPaid(amountPaid);
        payment.setPaymentMethod(paymentMethod);
        payment.setStatus("PAID");
        payment.setSuccessful(true);
        payment.setPaymentDate(LocalDateTime.now());
        order.setPaymentStatus("PAID");
        orderRepository.save(order);

        // Create Bakong transaction record
  
    Bakong bakong = new Bakong();
    bakong.setHash(bakong.getHash()); 
    bakong.setFromAccountId(bakong.getFromAccountId()); 
    bakong.setToAccountId(bakong.getToAccountId()); 
    bakong.setCurrency(bakong.currency); 
    bakong.setAmount(bakong.getAmount());
    bakong.setDescription(bakong.getDescription());
    bakong.setCreatedDateMs(bakong.getCreatedDateMs()); 
    bakong.setAcknowledgedDateMs(bakong.getAcknowledgedDateMs());
    bakong.setTrackingStatus(null); 
    bakong.setReceiverBank(null); 
    bakong.setReceiverBankAccount(null); 
    bakong.setInstructionRef(null); 
    bakong.setExternalRef(bakong.getExternalRef()); 

        bakong.setPayment(payment); 

        bakongRepository.save(bakong); 

        return paymentRepository.save(payment);
    }

}
