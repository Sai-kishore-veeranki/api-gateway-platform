package com.example.userservice.service;

import com.example.userservice.dto.UserRequest;
import com.example.userservice.dto.UserResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserService {

    private final ConcurrentMap<Long, UserResponse> users = new ConcurrentHashMap<>();
    private final AtomicLong idSequence = new AtomicLong(3);

    public UserService() {
        users.put(1L, new UserResponse(1, "Alice Johnson", "alice@example.com"));
        users.put(2L, new UserResponse(2, "Bob Smith", "bob@example.com"));
        users.put(3L, new UserResponse(3, "Carol Davis", "carol@example.com"));
    }

    public List<UserResponse> getAllUsers() {
        return List.copyOf(users.values());
    }

    public Optional<UserResponse> getUserById(long id) {
        return Optional.ofNullable(users.get(id));
    }

    public UserResponse createUser(UserRequest request) {
        long id = idSequence.incrementAndGet();
        UserResponse user = new UserResponse(id, request.name(), request.email());
        users.put(id, user);
        return user;
    }
}
