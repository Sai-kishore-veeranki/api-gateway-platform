package com.vsk.inventoryservice.controller;

import com.vsk.inventoryservice.dto.InventoryResponse;
import com.vsk.inventoryservice.service.InventoryService;
import jakarta.validation.constraints.Positive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private static final Logger log = LoggerFactory.getLogger(InventoryController.class);
    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public List<InventoryResponse> getAllInventory() {
        log.info("GET /inventory requested");
        return inventoryService.getAllInventory();
    }


    @GetMapping("/{productId}")
    public InventoryResponse getInventory(@PathVariable long productId) throws Exception {
        log.info("GET /inventory/{} requested", productId);
        return inventoryService.getInventory(productId);
    }

    @PutMapping("/{productId}/reduce")
    public InventoryResponse reduceInventory(
            @PathVariable long productId,
            @RequestParam @Positive int quantity) throws Exception {
        log.info("PUT /inventory/{}/reduce requested for quantity {}", productId, quantity);
        return inventoryService.reduceInventory(productId, quantity);
    }
}
