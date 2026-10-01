package com.example.orderservice.dto;

public record PaymentResponse(long paymentId, long orderId, String status) {
}
