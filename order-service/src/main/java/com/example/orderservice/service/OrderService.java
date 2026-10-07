package com.example.orderservice.service;

import com.example.orderservice.client.InventoryClient;
import com.example.orderservice.client.PaymentClient;
import com.example.orderservice.client.UserClient;
import com.example.orderservice.dto.InventoryResponse;
import com.example.orderservice.dto.PaymentRequest;
import com.example.orderservice.dto.PaymentResponse;
import com.example.orderservice.model.CreateOrderRequest;
import com.example.orderservice.model.OrderResponse;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private final UserClient userClient;
    private final InventoryClient inventoryClient;
    private final PaymentClient paymentClient;
    private final ConcurrentMap<Long, OrderResponse> orders = new ConcurrentHashMap<>();
    private final AtomicLong idSequence = new AtomicLong(1000);

    public OrderService(
            UserClient userClient,
            InventoryClient inventoryClient,
            PaymentClient paymentClient) {
        this.userClient = userClient;
        this.inventoryClient = inventoryClient;
        this.paymentClient = paymentClient;
    }

    public List<OrderResponse> getAllOrders() {
        log.info("Fetching all orders: {} total", orders.size());
        return List.copyOf(orders.values());
    }

    public Optional<OrderResponse> getOrderById(long id) {
        log.info("Looking up order by id {}", id);
        return Optional.ofNullable(orders.get(id));
    }

    public OrderResponse createOrder(CreateOrderRequest request) {
        log.info("Creating order for user {} product {} quantity {}", request.userId(), request.productId(), request.quantity());
        callUserService(request.userId());

        InventoryResponse inventory = callInventoryService(request.productId());
        if (inventory.availableQuantity() < request.quantity()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Insufficient inventory");
        }

        BigDecimal amount = inventory.unitPrice().multiply(BigDecimal.valueOf(request.quantity()));
        long orderId = idSequence.incrementAndGet();
        OrderResponse pendingOrder = new OrderResponse(
                orderId, request.userId(), request.productId(), request.quantity(), amount, "PENDING");
        orders.put(orderId, pendingOrder);

        PaymentResponse payment = callPaymentService(
                new PaymentRequest(orderId, request.userId(), amount));
        if (!"SUCCESS".equalsIgnoreCase(payment.status())) {
            orders.put(orderId, withStatus(pendingOrder, "PAYMENT_FAILED"));
            throw new ResponseStatusException(HttpStatus.PAYMENT_REQUIRED, "Payment was declined");
        }

        callInventoryReduction(request.productId(), request.quantity());
        OrderResponse confirmedOrder = withStatus(pendingOrder, "CONFIRMED");
        orders.put(orderId, confirmedOrder);
        log.info("Order {} confirmed successfully", orderId);
        return confirmedOrder;
    }

    private void callUserService(long userId) {
        try {
            userClient.getUserById(userId);
        } catch (FeignException exception) {
            throw downstreamFailure("User service", exception);
        }
    }

    private InventoryResponse callInventoryService(long productId) {
        try {
            return inventoryClient.getInventory(productId);
        } catch (FeignException exception) {
            throw downstreamFailure("Inventory service", exception);
        }
    }

    private PaymentResponse callPaymentService(PaymentRequest request) {
        try {
            return paymentClient.processPayment(request);
        } catch (FeignException exception) {
            throw downstreamFailure("Payment service", exception);
        }
    }

    private void callInventoryReduction(long productId, int quantity) {
        try {
            inventoryClient.reduceInventory(productId, quantity);
        } catch (FeignException exception) {
            throw downstreamFailure("Inventory service", exception);
        }
    }

    private ResponseStatusException downstreamFailure(String service, FeignException exception) {
        if (exception.status() == HttpStatus.NOT_FOUND.value()) {
            return new ResponseStatusException(HttpStatus.NOT_FOUND, service + " resource not found");
        }
        return new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE, service + " is unavailable");
    }

    private OrderResponse withStatus(OrderResponse order, String status) {
        return new OrderResponse(
                order.orderId(),
                order.userId(),
                order.productId(),
                order.quantity(),
                order.amount(),
                status);
    }
}
