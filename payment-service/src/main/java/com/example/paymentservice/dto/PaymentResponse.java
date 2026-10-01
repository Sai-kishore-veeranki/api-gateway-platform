package com.example.paymentservice.dto;

public record PaymentResponse(long paymentId, long orderId, String status) {
}
