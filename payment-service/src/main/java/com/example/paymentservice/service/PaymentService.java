package com.example.paymentservice.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class PaymentService {

    private final List<Map<String, Object>> payments = List.of(
            Map.of("id", "pay-1", "orderId", 1, "method", "CARD", "amount", 89.99, "status", "CAPTURED"),
            Map.of("id", "pay-2", "orderId", 2, "method", "PAYPAL", "amount", 45.50, "status", "PENDING"));

    public List<Map<String, Object>> getAllPayments() {
        return payments;
    }

    public Optional<Map<String, Object>> getPaymentById(String id) {
        return payments.stream()
                .filter(p -> p.get("id").equals(id))
                .findFirst();
    }

    /**
     * Randomly throws ~50% of the time. Handy for demonstrating the
     * gateway's retry + circuit breaker + fallback behaviour:
     *   curl -H "X-API-Key: <key>" http://localhost:8080/api/payments/flaky
     */
    public Map<String, Object> flakyPayment() {
        if (ThreadLocalRandom.current().nextBoolean()) {
            throw new IllegalStateException("Payment provider timeout (simulated)");
        }
        return Map.of("id", "pay-" + System.currentTimeMillis(),
                "status", "CAPTURED",
                "note", "lucky call - payment provider succeeded");
    }
}
