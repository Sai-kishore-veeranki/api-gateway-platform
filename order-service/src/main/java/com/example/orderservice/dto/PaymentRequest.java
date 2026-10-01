package com.example.orderservice.dto;

import java.math.BigDecimal;

public record PaymentRequest(long orderId, long userId, BigDecimal amount) {
}
