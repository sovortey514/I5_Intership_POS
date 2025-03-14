package com.twd.Pos.dto;

import lombok.Data;
import java.util.List;

@Data
public class OrderRequest {
    private Long userId;
    private Long tableId;
    private List<OrderItemRequest> items;  // ✅ Change `orderItems` → `items`
}
