package com.example.GymManagementBackend.service;

import com.example.GymManagementBackend.exception.DuplicateResourceException;
import com.example.GymManagementBackend.exception.MaxCapacityReachedException;
import com.example.GymManagementBackend.model.Booking;
import com.example.GymManagementBackend.model.GymClass;
import com.example.GymManagementBackend.model.User;
import com.example.GymManagementBackend.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;

    /**
     * Handles the core logic of creating a new booking.
     * This method is responsible for checking business rules and saving the booking.
     *
     * @param user The user who is booking the class.
     * @param gymClass The class being booked.
     * @return The newly created Booking entity.
     */
    @Transactional
    public Booking createBooking(User user, GymClass gymClass) {
        // Step 1: Check for existing booking to prevent duplicates
        if (bookingRepository.findByUserAndGymClass(user, gymClass).isPresent()) {
            throw new DuplicateResourceException("You have already booked this class.");
        }

        // Step 2: Check if class is full
        if (gymClass.getBookings().size() >= gymClass.getCapacity()) {
            throw new MaxCapacityReachedException("This class is already full.");
        }

        // Step 3: Create and save the new booking
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setGymClass(gymClass);

        return bookingRepository.save(booking);
    }
}