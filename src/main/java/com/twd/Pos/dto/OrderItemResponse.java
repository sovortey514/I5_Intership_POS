package com.twd.Pos.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class OrderItemResponse {
     private Long foodId;
    private String foodName;     
    private String foodDescription;
    private int quantity;
    private BigDecimal price;
    private BigDecimal totalPrice;
    
}
