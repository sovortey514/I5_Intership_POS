package com.twd.Pos.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderResponse {

    private Long id;
    private String customOrderId;
    private BigDecimal total;
    private String status;
    private String paymentStatus;
    private List<OrderItemResponse> orderItems;

    @Data
    public static class OrderItemResponse {
        private Long foodId;
        private String foodName;
        private String foodDescription;
        private int quantity;
        private BigDecimal price;
        private BigDecimal totalPrice;
    }
}
