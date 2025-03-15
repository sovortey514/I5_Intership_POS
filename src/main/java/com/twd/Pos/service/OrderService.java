package com.twd.Pos.service;

import java.util.List;

import com.twd.Pos.dto.OrderItemRequest;
import com.twd.Pos.dto.OrderResponse;
import com.twd.Pos.entity.Order;

public interface OrderService {
    Order createOrder(Long userId, Long tableId, List<OrderItemRequest> itemRequests);
    Order completeOrder(Long orderId);
    Order cancelOrder(Long orderId);
    Order getOrderById(Long orderId);
    List<Order> getAllOrders();
    Order addItemsToOrder(Long orderId, List<OrderItemRequest> itemRequests);
    Order removeItemsFromOrder(Long orderId, List<Long> itemIds);
    List<OrderResponse> getAllOrdersAsList();
    List<OrderResponse> getOrderSummaryById(Long orderId);



}
