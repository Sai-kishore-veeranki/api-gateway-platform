package com.example.paymentservice.service;

import com.example.paymentservice.dto.PaymentRequest;
import com.example.paymentservice.dto.PaymentResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);
    private final List<Map<String, Object>> payments = List.of(
            Map.of("id", "pay-1", "orderId", 1, "method", "CARD", "amount", 89.99, "status", "CAPTURED"),
            Map.of("id", "pay-2", "orderId", 2, "method", "PAYPAL", "amount", 45.50, "status", "PENDING"));
    private final AtomicLong paymentIdSequence = new AtomicLong(100);

    public List<Map<String, Object>> getAllPayments() {
        log.info("Returning {} payment records", payments.size());
        return payments;
    }

    public Optional<Map<String, Object>> getPaymentById(String id) {
        log.info("Looking up payment id {}", id);
        return payments.stream()
                .filter(payment -> payment.get("id").equals(id))
                .findFirst();
    }

    public Map<String, Object> flakyPayment() {
        log.info("Attempting simulated payment provider call");
        if (ThreadLocalRandom.current().nextBoolean()) {
            log.warn("Payment provider timeout simulated");
            throw new IllegalStateException("Payment provider timeout (simulated)");
        }
        return Map.of("id", "pay-" + System.currentTimeMillis(),
                "status", "CAPTURED",
                "note", "lucky call - payment provider succeeded");
    }

    public PaymentResponse processPayment(PaymentRequest request) {
        String status = request.amount().signum() > 0 ? "SUCCESS" : "FAILED";
        log.info("Processing payment for order {} with status {}", request.orderId(), status);
        return new PaymentResponse(paymentIdSequence.incrementAndGet(), request.orderId(), status);
    }
}
