package com.example.orderservice.dto;

import java.math.BigDecimal;

public record InventoryResponse(
        long productId,
        String name,
        BigDecimal unitPrice,
        int availableQuantity) {
}
