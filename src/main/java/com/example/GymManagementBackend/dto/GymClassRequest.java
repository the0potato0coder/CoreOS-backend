package com.example.GymManagementBackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;

/**
 * A DTO for creating a new GymClass.
 * This record encapsulates the data required from the client to schedule a class.
 */
public record GymClassRequest(
        @NotBlank String name,
        @NotNull @Positive Long trainerId,
        @NotNull LocalDateTime schedule,
        @NotNull @Positive Integer durationInMinutes,
        @NotNull @Positive Integer capacity
) {}
