package com.twd.Pos.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class PaymentOrderDTO {
    private Long paymentId;
    private String paymentMethod;
    private BigDecimal amountPaid;
    private LocalDateTime paymentDate;
    private String orderId;
    private BigDecimal orderTotal; 
    private Long membershipId;
    private BigDecimal cashBack; 
    private String orderStatus; 
    private Long tableId;


    public PaymentOrderDTO(Long paymentId, String paymentMethod, BigDecimal amountPaid, LocalDateTime paymentDate, String orderId, BigDecimal orderTotal,Long membershipId) {
        this.paymentId = paymentId;
        this.paymentMethod = paymentMethod;
        this.amountPaid = amountPaid;
        this.paymentDate = paymentDate;
        this.orderId = orderId;
        this.orderTotal = orderTotal;
        this.membershipId = membershipId;
    }

}
