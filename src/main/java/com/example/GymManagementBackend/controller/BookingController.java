package com.example.GymManagementBackend.controller;

import com.example.GymManagementBackend.dto.BookingRequest;
import com.example.GymManagementBackend.model.Booking;
import com.example.GymManagementBackend.service.GymClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BookingController {

    private final GymClassService gymClassService;

    /**
     * Allows a member to book a gym class.
     * This endpoint is for members only.
     * @param request The DTO containing the user ID and the class ID.
     * @return A ResponseEntity with the newly created Booking entity.
     */
    @PostMapping("/members/book-class")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<Booking> bookClass(@Valid @RequestBody BookingRequest request) {
        Booking newBooking = gymClassService.bookClass(request.userId(), request.classId());
        return new ResponseEntity<>(newBooking, HttpStatus.CREATED);
    }
}
