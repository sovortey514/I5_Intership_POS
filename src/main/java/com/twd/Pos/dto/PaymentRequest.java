package com.twd.Pos.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class PaymentRequest {
    private Long orderId;
    private BigDecimal amountPaid;
    private String paymentMethod;
    private String membershipId;
}