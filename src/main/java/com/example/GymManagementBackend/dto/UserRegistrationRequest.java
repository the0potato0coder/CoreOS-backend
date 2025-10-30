package com.example.GymManagementBackend.dto;

import java.time.LocalDate;

public record UserRegistrationRequest(
        String firstName,
        String lastName,
        String email,
        String password,
        LocalDate dateOfBirth
) {}
