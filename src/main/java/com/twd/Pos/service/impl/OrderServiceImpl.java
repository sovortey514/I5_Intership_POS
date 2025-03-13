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

            Optional<Order> optionalOrder = orderRepository.findById(orderId);
            if (optionalOrder.isEmpty()) {
                throw new RuntimeException("Order not found");
            }
            Order order = optionalOrder.get();

            if (!order.getStatus().equalsIgnoreCase("PENDING")) {
                throw new RuntimeException("Only PENDING orders can be canceled");
            }

            order.setStatus("CANCELLED");

            Tables table = order.getTable();
            table.setStatus("available");
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
            if (!order.getPaymentStatus().equalsIgnoreCase("PAID")) {
                throw new RuntimeException("Order must be paid before completion");
            }

            order.setStatus("COMPLETED");

            Tables table = order.getTable();
            table.setStatus("available");
            tableRepository.save(table);

            return orderRepository.save(order);
        } catch (Exception e) {
            System.err.println("❌ Error in completeOrder: " + e.getMessage());
            return null;
        }
    }
    @Override
    @Transactional
    public Order createOrder(Long userId, Long tableId, List<OrderItem> orderItems) {
        try {

            OurUsers user = ourUserRepo.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Tables table = tableRepository.findById(tableId)
                    .orElseThrow(() -> new RuntimeException("Table not found"));

            if (!table.getStatus().equalsIgnoreCase("AVAILABLE")) {
                throw new RuntimeException("Table is not available");
            }

            table.setStatus("OCCUPIED");
            tableRepository.save(table);

            Order order = new Order();
            order.setUser(user);
            order.setTable(table);
            order.setStatus("PENDING");
            order.setPaymentStatus("UNPAID");

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

            orderRepository.save(order);
            orderItemRepository.saveAll(orderItems);

            return order;
        } catch (Exception e) {
            System.err.println("❌ Error in createOrder: " + e.getMessage());
            throw new RuntimeException("Failed to create order: " + e.getMessage());
        }
    }

    @Transactional
    @Override
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Transactional
    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
