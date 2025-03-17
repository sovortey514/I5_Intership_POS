package com.twd.Pos.dto;

import java.util.List;

import lombok.Data;

@Data
public class OrderEditRequest {
    private Long tableId;
    private List<OrderItemRequest> items;
}
