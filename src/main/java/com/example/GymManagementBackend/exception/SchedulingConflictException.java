package com.example.GymManagementBackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) // 409
public class SchedulingConflictException extends RuntimeException {
    public SchedulingConflictException(String message) {
        super(message);
    }
}
