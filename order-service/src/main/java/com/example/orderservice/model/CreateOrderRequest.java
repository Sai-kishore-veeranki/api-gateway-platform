package com.example.orderservice.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CreateOrderRequest(
        @NotBlank(message = "item is required") String item,
        @Positive(message = "amount must be positive") double amount) {
}
