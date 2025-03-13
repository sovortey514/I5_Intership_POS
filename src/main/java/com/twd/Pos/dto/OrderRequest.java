package com.twd.Pos.dto;

import com.twd.Pos.entity.OrderItem;
import lombok.Data;
import java.util.List;

@Data
public class OrderRequest {
    private Long userId;
    private Long tableId;
    private List<OrderItem> orderItems;

    @Data
    public static class OrderItemRequest {
        private Long foodId;
        private int quantity;
    }
}
