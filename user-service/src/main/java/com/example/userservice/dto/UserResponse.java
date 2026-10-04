package com.example.userservice.dto;

public record UserResponse(long id, String name, String email) implements java.io.Serializable {
}
