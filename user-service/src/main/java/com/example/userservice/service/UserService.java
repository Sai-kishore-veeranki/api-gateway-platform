package com.example.userservice.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private final List<Map<String, Object>> users = List.of(
            Map.of("id", 1, "name", "Alice Johnson", "email", "alice@example.com"),
            Map.of("id", 2, "name", "Bob Smith", "email", "bob@example.com"),
            Map.of("id", 3, "name", "Carol Davis", "email", "carol@example.com"));

    public List<Map<String, Object>> getAllUsers() {
        return users;
    }

    public Optional<Map<String, Object>> getUserById(long id) {
        return users.stream()
                .filter(u -> ((Number) u.get("id")).longValue() == id)
                .findFirst();
    }
}
