package com.vsk.inventoryservice;

import com.vsk.inventoryservice.dto.InventoryResponse;
import com.vsk.inventoryservice.service.InventoryService;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InventoryServiceTests {

    private final InventoryService inventoryService = new InventoryService();

    @Test
    void getInventoryReturnsSeededProduct() {
        InventoryResponse product = inventoryService.getInventory(10);

        assertEquals("Mechanical keyboard", product.name());
        assertEquals(25, product.availableQuantity());
    }

    @Test
    void reduceInventoryReturnsUpdatedQuantity() {
        InventoryResponse product = inventoryService.reduceInventory(10, 2);

        assertEquals(23, product.availableQuantity());
        assertEquals(23, inventoryService.getInventory(10).availableQuantity());
    }

    @Test
    void getInventoryRejectsUnknownProduct() {
        assertThrows(ResponseStatusException.class, () -> inventoryService.getInventory(99));
    }
}
