package com.twd.Pos.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.twd.Pos.dto.OrderItemRequest;
import com.twd.Pos.dto.OrderRequest;
import com.twd.Pos.dto.OrderResponse;
import com.twd.Pos.entity.Order;
import com.twd.Pos.entity.OrderItem;
import com.twd.Pos.service.OrderService;

@RestController
@RequestMapping("/auth")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // @PostMapping("/create")
    // public Order createOrder(@RequestBody OrderRequest request) {
    // return orderService.createOrder(request.getUserId(), request.getTableId(),
    // request.getItems());
    // }
    @PostMapping("/create")
    public OrderResponse createOrder(@RequestBody OrderRequest orderRequest) {
        // Call the service to create the order
        Order order = orderService.createOrder(orderRequest.getUserId(), orderRequest.getTableId(),
                orderRequest.getItems());

        // Convert Order to OrderResponse and send it back
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setId(order.getId());
        orderResponse.setCustomOrderId(order.getCustomOrderId());
        orderResponse.setTotal(order.getTotal());
        orderResponse.setStatus(order.getStatus());
        orderResponse.setPaymentStatus(order.getPaymentStatus());

        // Create OrderItemResponse and add food details
        List<OrderResponse.OrderItemResponse> orderItemResponses = new ArrayList<>();
        for (OrderItem orderItem : order.getOrderItems()) {
            OrderResponse.OrderItemResponse orderItemResponse = new OrderResponse.OrderItemResponse();
            orderItemResponse.setFoodId(orderItem.getFood().getId());
            orderItemResponse.setFoodName(orderItem.getFood().getName());
            orderItemResponse.setFoodDescription(orderItem.getFood().getDescription());
            orderItemResponse.setQuantity(orderItem.getQuantity());
            orderItemResponse.setPrice(orderItem.getPrice());
            orderItemResponse.setTotalPrice(orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())));

            orderItemResponses.add(orderItemResponse);
        }
        orderResponse.setOrderItems(orderItemResponses);

        return orderResponse; // Send the response with food details
    }

    @PutMapping("/complete/{orderId}")
    public Order completeOrder(@PathVariable Long orderId) {
        return orderService.completeOrder(orderId);
    }

    @PutMapping("/cancel/{orderId}")
    public Order cancelOrder(@PathVariable Long orderId) {
        return orderService.cancelOrder(orderId);
    }

    @GetMapping("/getOrder/{orderId}")
    public Order getOrderById(@PathVariable Long orderId) {
        return orderService.getOrderById(orderId);
    }

    @GetMapping("/getallorder")
    public List<Order> getallorder() {
        return orderService.getAllOrders();
    }

    @PutMapping("/{orderId}/add-items")
    public ResponseEntity<Order> addItemsToOrder(@PathVariable Long orderId,
            @RequestBody List<OrderItemRequest> itemRequests) {
        try {
            Order updatedOrder = orderService.addItemsToOrder(orderId, itemRequests);
            return ResponseEntity.ok(updatedOrder);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{orderId}/remove-items")
    public ResponseEntity<Order> removeItemsFromOrder(@PathVariable Long orderId, @RequestBody List<Long> itemIds) {
        try {
            Order updatedOrder = orderService.removeItemsFromOrder(orderId, itemIds);
            return ResponseEntity.ok(updatedOrder);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/getallorders")
    public ResponseEntity<List<OrderResponse>> getAllOrdersAsList() {
        try {
            List<OrderResponse> orders = orderService.getAllOrdersAsList();
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
