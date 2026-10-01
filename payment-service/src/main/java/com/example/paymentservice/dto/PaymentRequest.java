package com.example.paymentservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record PaymentRequest(
        @Positive long orderId,
        @Positive long userId,
        @NotNull BigDecimal amount) {
}
