package com.example.GymManagementBackend.dto;

/**
 * A DTO for sending back the JWT upon successful authentication.
 */
public record AuthResponse(String token) {
}
