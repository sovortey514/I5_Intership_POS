package com.twd.Pos.controller;

import com.twd.Pos.entity.OrderItem;
import com.twd.Pos.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;

    @GetMapping("/order-items/{orderId}")
    public ResponseEntity<List<OrderItem>> getOrderItemsByOrderId(@PathVariable Long orderId) {
        try {
            List<OrderItem> orderItems = orderItemService.getOrderItemsByOrderId(orderId);
            return ResponseEntity.ok(orderItems);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}