package com.twd.Pos.service;

import java.util.List;

import com.twd.Pos.dto.OrderRequest;
import com.twd.Pos.entity.Order;
import com.twd.Pos.entity.OrderItem;

public interface OrderService {
    
    // Order createOrder(Long userId, Long tableId, List<OrderRequest.OrderItemRequest> orderItemRequests);
    Order createOrder(Long userId, Long tableId, List<OrderItem> orderItems);
    Order completeOrder(Long orderId);
    Order cancelOrder(Long orderId);
}
