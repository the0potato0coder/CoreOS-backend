package com.example.GymManagementBackend.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * A DTO for making a payment.
 * This record encapsulates the data required from the client to initiate a payment.
 */
public record PaymentRequest(
        @NotNull @Positive Long userId,
        @NotNull @Positive Double amount
) {}
