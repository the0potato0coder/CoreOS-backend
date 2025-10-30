package com.example.GymManagementBackend.service;

import com.example.GymManagementBackend.exception.DuplicateResourceException;
import com.example.GymManagementBackend.exception.MaxCapacityReachedException;
import com.example.GymManagementBackend.model.Booking;
import com.example.GymManagementBackend.model.GymClass;
import com.example.GymManagementBackend.model.User;
import com.example.GymManagementBackend.repository.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BookingService bookingService;

    private User user;
    private GymClass gymClass;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        gymClass = new GymClass();
        gymClass.setId(1L);
        gymClass.setCapacity(20);
        gymClass.setBookings(new HashSet<>());
    }

    @Test
    void createBooking_shouldCreateSuccessfully() {
        when(bookingRepository.findByUserAndGymClass(user, gymClass)).thenReturn(Optional.empty());
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Booking result = bookingService.createBooking(user, gymClass);

        assertNotNull(result);
        assertEquals(user, result.getUser());
        assertEquals(gymClass, result.getGymClass());
    }

    @Test
    void createBooking_shouldThrowExceptionWhenAlreadyBooked() {
        when(bookingRepository.findByUserAndGymClass(user, gymClass)).thenReturn(Optional.of(new Booking()));

        assertThrows(DuplicateResourceException.class, () -> {
            bookingService.createBooking(user, gymClass);
        });
    }

    @Test
    void createBooking_shouldThrowExceptionWhenClassIsFull() {
        // Simulate a full class by setting capacity equal to the number of bookings
        gymClass.setCapacity(1);
        Set<Booking> bookings = new HashSet<>();
        bookings.add(new Booking());
        gymClass.setBookings(bookings);

        when(bookingRepository.findByUserAndGymClass(user, gymClass)).thenReturn(Optional.empty());

        assertThrows(MaxCapacityReachedException.class, () -> {
            bookingService.createBooking(user, gymClass);
        });
    }

    @Test
    void createBooking_shouldSucceedWhenClassIsAlmostFull() {
        // Simulate a nearly full class
        gymClass.setCapacity(2);
        Set<Booking> bookings = new HashSet<>();
        bookings.add(new Booking()); // One booking already exists
        gymClass.setBookings(bookings);

        when(bookingRepository.findByUserAndGymClass(user, gymClass)).thenReturn(Optional.empty());
        when(bookingRepository.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Booking result = bookingService.createBooking(user, gymClass);

        assertNotNull(result);
        assertEquals(user, result.getUser());
        assertEquals(gymClass, result.getGymClass());
    }
}