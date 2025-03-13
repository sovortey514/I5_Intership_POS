package com.twd.Pos.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.twd.Pos.dto.OrderRequest;
import com.twd.Pos.entity.Food;
import com.twd.Pos.entity.Order;
import com.twd.Pos.entity.OrderItem;
import com.twd.Pos.entity.OurUsers;
import com.twd.Pos.entity.Tables;
import com.twd.Pos.repository.FoodRepository;
import com.twd.Pos.repository.OrderItemRepository;
import com.twd.Pos.repository.OrderRepository;
import com.twd.Pos.repository.OurUserRepo;
import com.twd.Pos.repository.TableRepository;
import com.twd.Pos.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OurUserRepo ourUserRepo;

    @Autowired
    private TableRepository tableRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Transactional
    @Override
    public Order cancelOrder(Long orderId) {
        try {
            // Find Order
            Optional<Order> optionalOrder = orderRepository.findById(orderId);
            if (optionalOrder.isEmpty()) {
                throw new RuntimeException("Order not found");
            }
            Order order = optionalOrder.get();

            // Validate Order Status
            if (!order.getStatus().equalsIgnoreCase("PENDING")) {
                throw new RuntimeException("Only PENDING orders can be canceled");
            }

            // Mark Order as CANCELLED
            order.setStatus("CANCELLED");

            // Free Up Table
            Tables table = order.getTable();
            table.setStatus("AVAILABLE");
            tableRepository.save(table);

            return orderRepository.save(order);
        } catch (Exception e) {
            System.err.println("❌ Error in cancelOrder: " + e.getMessage());
            return null;
        }
    }

    @Transactional
    @Override
    public Order completeOrder(Long orderId) {
        try {
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found"));

            // Ensure the order is paid before completing
            if (!order.getPaymentStatus().equalsIgnoreCase("PAID")) {
                throw new RuntimeException("Order must be paid before completion");
            }

            order.setStatus("COMPLETED");

            // Free Up Table
            Tables table = order.getTable();
            table.setStatus("AVAILABLE");
            tableRepository.save(table);

            return orderRepository.save(order);
        } catch (Exception e) {
            System.err.println("❌ Error in completeOrder: " + e.getMessage());
            return null;
        }
    }

//     @Override
// @Transactional
// public Order createOrder(Long userId, Long tableId, List<OrderRequest.OrderItemRequest> orderItemRequests) {
//     try {
//         // Validate User
//         OurUsers user = ourUserRepo.findById(userId)
//                 .orElseThrow(() -> new RuntimeException("User not found"));

//         // Validate Table
//         Tables table = tableRepository.findById(tableId)
//                 .orElseThrow(() -> new RuntimeException("Table not found"));

//         if (!table.getStatus().equalsIgnoreCase("AVAILABLE")) {
//             throw new RuntimeException("Table is not available");
//         }

//         // Mark Table as OCCUPIED
//         table.setStatus("OCCUPIED");
//         tableRepository.save(table);

//         // Create Order
//         Order order = new Order();
//         order.setUser(user);
//         order.setTable(table);
//         order.setStatus("PENDING");
//         order.setPaymentStatus("UNPAID");

//         // Calculate Total Price & Create OrderItems
//         BigDecimal total = BigDecimal.ZERO;
//         List<OrderItem> orderItems = new ArrayList<>();

//         for (OrderRequest.OrderItemRequest itemRequest : orderItemRequests) {
//             Food food = foodRepository.findById(itemRequest.getFoodId())
//                     .orElseThrow(() -> new RuntimeException("Food item not found"));

//             OrderItem item = new OrderItem();
//             item.setOrder(order);
//             item.setFood(food);
//             item.setQuantity(itemRequest.getQuantity());
//             item.setPrice(food.getPrice());

//             total = total.add(food.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity())));
//             orderItems.add(item);
//         }

//         order.setOrderItems(orderItems);
//         order.setTotal(total);

//         // Save Order and Items
//         orderRepository.save(order);
//         orderItemRepository.saveAll(orderItems);

//         return order;
//     } catch (Exception e) {
//         System.err.println("❌ Error in createOrder: " + e.getMessage());
//         throw new RuntimeException("Failed to create order: " + e.getMessage());
//     }
// }

@Override
@Transactional
public Order createOrder(Long userId, Long tableId, List<OrderItem> orderItems) {
    try {
        // Validate User
        OurUsers user = ourUserRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Validate Table
        Tables table = tableRepository.findById(tableId)
                .orElseThrow(() -> new RuntimeException("Table not found"));

        if (!table.getStatus().equalsIgnoreCase("AVAILABLE")) {
            throw new RuntimeException("Table is not available");
        }

        // Mark Table as OCCUPIED
        table.setStatus("OCCUPIED");
        tableRepository.save(table);

        // Create Order
        Order order = new Order();
        order.setUser(user);
        order.setTable(table);
        order.setStatus("PENDING");
        order.setPaymentStatus("UNPAID");

        // Calculate Total Price & Validate `Food` in `OrderItem`
        BigDecimal total = BigDecimal.ZERO;
        for (OrderItem item : orderItems) {
            Food food = foodRepository.findById(item.getFood().getId())
                    .orElseThrow(() -> new RuntimeException("Food item not found"));

            item.setOrder(order);
            item.setPrice(food.getPrice());
            total = total.add(food.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        order.setOrderItems(orderItems);
        order.setTotal(total);

        // Save Order and Items
        orderRepository.save(order);
        orderItemRepository.saveAll(orderItems);

        return order;
    } catch (Exception e) {
        System.err.println("❌ Error in createOrder: " + e.getMessage());
        throw new RuntimeException("Failed to create order: " + e.getMessage());
    }
}


}
