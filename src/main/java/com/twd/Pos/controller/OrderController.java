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

import com.twd.Pos.dto.OrderEditRequest;
import com.twd.Pos.dto.OrderItemRequest;
import com.twd.Pos.dto.OrderRequest;
import com.twd.Pos.dto.OrderResponse;
import com.twd.Pos.entity.Order;
import com.twd.Pos.entity.OrderItem;
import com.twd.Pos.entity.Tables;
import com.twd.Pos.service.OrderService;

@RestController
@RequestMapping("/auth")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/placeordercontroller")
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

    @GetMapping("/getordersummary/{orderId}")
    public ResponseEntity<List<OrderResponse>> getOrderSummaryById(@PathVariable Long orderId) {
        try {
            List<OrderResponse> orders = orderService.getOrderSummaryById(orderId);
            if (orders.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{orderId}/edit")
    public ResponseEntity<?> editOrder(
            @PathVariable Long orderId,
            @RequestBody OrderEditRequest orderEditRequest) {

        try {
            Order updatedOrder = orderService.editOrder(orderId, orderEditRequest.getTableId(),
                    orderEditRequest.getItems());

            // Convert to OrderResponse
            OrderResponse orderResponse = new OrderResponse();
            orderResponse.setId(updatedOrder.getId());
            orderResponse.setCustomOrderId(updatedOrder.getCustomOrderId());
            orderResponse.setTotal(updatedOrder.getTotal());
            orderResponse.setStatus(updatedOrder.getStatus());
            orderResponse.setPaymentStatus(updatedOrder.getPaymentStatus());

            Tables table = updatedOrder.getTable();
            if (table != null) {
                orderResponse.setTableId(table.getId());
                orderResponse.setTableName(table.getName());
                orderResponse.setTableType(table.getType());
                orderResponse.setTableLocation(table.getLocation());
            }

            // Convert Order Items to OrderItemResponse
            List<OrderResponse.OrderItemResponse> orderItemResponses = new ArrayList<>();
            for (OrderItem orderItem : updatedOrder.getOrderItems()) {
                OrderResponse.OrderItemResponse orderItemResponse = new OrderResponse.OrderItemResponse();
                orderItemResponse.setFoodId(orderItem.getFood().getId());
                orderItemResponse.setFoodName(orderItem.getFood().getName());
                orderItemResponse.setFoodDescription(orderItem.getFood().getDescription());
                orderItemResponse.setQuantity(orderItem.getQuantity());
                orderItemResponse.setPrice(orderItem.getPrice());
                orderItemResponse
                        .setTotalPrice(orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())));
                orderItemResponses.add(orderItemResponse);
            }
            orderResponse.setOrderItems(orderItemResponses);
            return ResponseEntity.ok(orderResponse);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("❌ Invalid request: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ An unexpected error occurred. Please try again.");
        }
    }

}
