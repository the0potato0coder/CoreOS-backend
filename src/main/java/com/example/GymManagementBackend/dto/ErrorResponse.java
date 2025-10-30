package com.example.GymManagementBackend.dto;

import java.time.LocalDateTime;

// A simple record to structure our error responses
public record ErrorResponse(String message, LocalDateTime timestamp) {}
