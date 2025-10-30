package com.example.GymManagementBackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // 404
public class WorkoutPlanNotFoundException extends RuntimeException {
    public WorkoutPlanNotFoundException(String message) {
        super(message);
    }
}
