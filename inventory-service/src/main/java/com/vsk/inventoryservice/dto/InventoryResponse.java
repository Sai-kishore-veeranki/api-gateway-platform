package com.vsk.inventoryservice.dto;

import java.math.BigDecimal;

public record InventoryResponse(
        long productId,
        String name,
        BigDecimal unitPrice,
        int availableQuantity) implements java.io.Serializable {
}
