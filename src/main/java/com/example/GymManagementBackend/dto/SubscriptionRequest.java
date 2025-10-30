package com.example.GymManagementBackend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SubscriptionRequest(
        @NotNull @Positive Long userId,
        @NotNull @Positive Long planId
) {}
