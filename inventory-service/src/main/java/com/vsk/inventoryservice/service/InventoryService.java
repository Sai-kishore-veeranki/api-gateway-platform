package com.vsk.inventoryservice.service;

import com.vsk.inventoryservice.dto.InventoryResponse;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class InventoryService {

    private final ConcurrentMap<Long, InventoryResponse> inventory = new ConcurrentHashMap<>();

    public InventoryService() {
        inventory.put(10L, new InventoryResponse(10, "Mechanical keyboard", new BigDecimal("89.99"), 25));
        inventory.put(11L, new InventoryResponse(11, "USB-C hub", new BigDecimal("45.50"), 40));
    }

    @Cacheable(cacheNames = "inventory", key = "'all'")
    public List<InventoryResponse> getAllInventory() {
        return List.copyOf(inventory.values());
    }

    @Cacheable(cacheNames = "inventoryByProduct", key = "#productId")
    public InventoryResponse getInventory(long productId) {
        InventoryResponse item = inventory.get(productId);
        if (item == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
        return item;
    }

    @CacheEvict(cacheNames = {"inventory", "inventoryByProduct"}, allEntries = true)
    public synchronized InventoryResponse reduceInventory(long productId, int quantity) {
        InventoryResponse item = getInventory(productId);
        if (quantity <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Quantity must be positive");
        }
        if (item.availableQuantity() < quantity) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Insufficient inventory");
        }
        InventoryResponse updated = new InventoryResponse(
                item.productId(),
                item.name(),
                item.unitPrice(),
                item.availableQuantity() - quantity);
        inventory.put(productId, updated);
        return updated;
    }
}
