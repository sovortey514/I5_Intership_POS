package com.twd.Pos.service;

import com.twd.Pos.entity.OrderItem;
import java.util.List;

public interface OrderItemService {
    List<OrderItem> saveOrderItems(List<OrderItem> orderItems);
    List<OrderItem> getOrderItemsByOrderId(Long orderId);
}
