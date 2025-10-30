package com.example.GymManagementBackend.dto;

/**
 * A DTO for capturing user login credentials.
 */
public record LoginRequest(String email, String password) {
}
