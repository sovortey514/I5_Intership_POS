package com.twd.Pos.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.twd.Pos.dto.OrderItemRequest;
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

    @Override
    @Transactional
    public Order createOrder(Long userId, Long tableId, List<OrderItemRequest> itemRequests) {
        if (itemRequests == null || itemRequests.isEmpty()) {
            throw new IllegalArgumentException("❌ Order must contain at least one valid item.");
        }

        OurUsers user = ourUserRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Tables table = tableRepository.findById(tableId)
                .orElseThrow(() -> new RuntimeException("Table not found"));

        if (!"AVAILABLE".equalsIgnoreCase(table.getStatus())) {
            throw new RuntimeException("Table is not available");
        }

        table.setStatus("OCCUPIED");
        tableRepository.save(table);

        Order order = new Order();
        order.setUser(user);
        order.setTable(table);
        order.setStatus("PENDING");
        order.setPaymentStatus("UNPAID");
        order.setTotal(BigDecimal.ZERO);

        order = orderRepository.saveAndFlush(order);

        order.setCustomOrderId(generateCustomOrderId(order.getId()));
    order = orderRepository.save(order);



        System.out.println("✅ Order ID after save: " + order.getId());

        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemRequest itemRequest : itemRequests) {
            Food food = foodRepository.findById(itemRequest.getFoodId())
                    .orElseThrow(() -> new RuntimeException("Food item not found"));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setFood(food);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setPrice(food.getPrice());

            BigDecimal itemTotal = food.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            total = total.add(itemTotal);
            orderItems.add(orderItem);
        }

        orderItemRepository.saveAll(orderItems);

        order.setOrderItems(orderItems);
        order.setTotal(total);

        return orderRepository.save(order);
    }
    private String generateCustomOrderId(Long id) {
        return "#" + String.format("%06d", id);
    }

    @Override
    public Order addItemsToOrder(Long orderId, List<OrderItemRequest> itemRequests) {
       try {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getStatus().equalsIgnoreCase("PENDING")) {
            throw new RuntimeException("Cannot modify a completed or cancelled order");
        }
        BigDecimal total = order.getTotal();
        List<OrderItem> additionalItems = new ArrayList<>();
        for (OrderItemRequest itemRequest : itemRequests) {
     
            Food food = foodRepository.findById(itemRequest.getFoodId())
                    .orElseThrow(() -> new RuntimeException("Food item not found"));

          
            OrderItem newItem = new OrderItem();
            newItem.setOrder(order);  
            newItem.setFood(food);
            newItem.setQuantity(itemRequest.getQuantity());
            newItem.setPrice(food.getPrice());

            BigDecimal itemTotal = food.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            total = total.add(itemTotal);

            additionalItems.add(newItem);

            order.getOrderItems().addAll(additionalItems);
        order.setTotal(total);

        orderRepository.save(order);
        orderItemRepository.saveAll(additionalItems);

        return order;
        }
         return order;
       } catch (Exception e) {
        System.err.println("❌ Error in addItemsToOrder: " + e.getMessage());
        throw new RuntimeException("Failed to update order items: " + e.getMessage());
       }
    }

    @Override
    @Transactional
    public Order removeItemsFromOrder(Long orderId, List<Long> itemIds) {
        try {
            // Retrieve the existing order
            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Order not found"));

            // Ensure order is still modifiable
            if (!order.getStatus().equalsIgnoreCase("PENDING")) {
                throw new RuntimeException("Cannot modify a completed or cancelled order");
            }

            // Fetch items to be removed and update order
            BigDecimal total = order.getTotal();
            List<OrderItem> itemsToRemove = new ArrayList<>();
            for (Long itemId : itemIds) {
                OrderItem orderItem = orderItemRepository.findById(itemId)
                        .orElseThrow(() -> new RuntimeException("Order item not found"));

                if (!orderItem.getOrder().getId().equals(orderId)) {
                    throw new RuntimeException("Order item does not belong to the specified order");
                }

                BigDecimal itemTotal = orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()));
                total = total.subtract(itemTotal);

                itemsToRemove.add(orderItem);
            }

            order.getOrderItems().removeAll(itemsToRemove);
            order.setTotal(total);

            orderRepository.save(order);
            orderItemRepository.deleteAll(itemsToRemove);

            return order;
        } catch (Exception e) {
            System.err.println("❌ Error in removeItemsFromOrder: " + e.getMessage());
            throw new RuntimeException("Failed to remove order items: " + e.getMessage());
        }
    }
}
