package com.example.orderservice.model;

import java.math.BigDecimal;

public record OrderResponse(
        long orderId,
        long userId,
        long productId,
        int quantity,
        BigDecimal amount,
        String status) {
}
