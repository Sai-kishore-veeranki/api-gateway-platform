package com.vsk.inventoryservice.api;

import java.util.Map;

public record ApiError(
        String timestamp,
        int status,
        String error,
        String message,
        String path,
        Map<String, String> validationErrors) {
}
