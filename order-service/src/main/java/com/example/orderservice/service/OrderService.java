package com.example.orderservice.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class OrderService {

    private final Map<Long, Map<String, Object>> orders = new ConcurrentHashMap<>();
    private final AtomicLong idSequence = new AtomicLong(1000);

    public OrderService() {
        orders.put(1L, Map.of("id", 1L, "item", "Mechanical keyboard", "amount", 89.99, "status", "SHIPPED"));
        orders.put(2L, Map.of("id", 2L, "item", "USB-C hub", "amount", 45.50, "status", "PROCESSING"));
    }

    public List<Map<String, Object>> getAllOrders() {
        return List.copyOf(orders.values());
    }

    public Optional<Map<String, Object>> getOrderById(long id) {
        return Optional.ofNullable(orders.get(id));
    }

    public Map<String, Object> createOrder(String item, double amount) {
        long id = idSequence.incrementAndGet();
        Map<String, Object> order = Map.of(
                "id", id,
                "item", item,
                "amount", amount,
                "status", "CREATED");
        orders.put(id, order);
        return order;
    }
}
