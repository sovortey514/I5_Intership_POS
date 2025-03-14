package com.twd.Pos.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class OrderItemResponse {
     private Long foodId;
    private String foodName;        // Add food name
    private String foodDescription; // Add food description
    private int quantity;
    private BigDecimal price;
    private BigDecimal totalPrice;
}
