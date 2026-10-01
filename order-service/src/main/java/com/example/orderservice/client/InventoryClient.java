package com.example.orderservice.client;

import com.example.orderservice.dto.InventoryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "inventory-service")
public interface InventoryClient {

    @GetMapping("/inventory/{productId}")
    InventoryResponse getInventory(@PathVariable("productId") long productId);

    @PutMapping("/inventory/{productId}/reduce")
    InventoryResponse reduceInventory(
            @PathVariable("productId") long productId,
            @RequestParam("quantity") int quantity);
}
