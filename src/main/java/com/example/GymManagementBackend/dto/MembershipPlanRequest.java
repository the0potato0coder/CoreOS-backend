package com.example.GymManagementBackend.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record MembershipPlanRequest (
        @NotBlank(message = "Membership name is required") @Size(max = 100) String name,
        @NotBlank String description,
        // Added the features field to the DTO.
        @NotBlank String features,
        @NotNull @Positive Double price,
        @NotNull @Positive Integer durationInDays
) {}

