package com.example.GymManagementBackend.service;

import com.example.GymManagementBackend.dto.GymClassRequest;
import com.example.GymManagementBackend.exception.ForbiddenException;
import com.example.GymManagementBackend.exception.MembershipExpiredException;
import com.example.GymManagementBackend.exception.ResourceNotFoundException;
import com.example.GymManagementBackend.exception.SchedulingConflictException;
import com.example.GymManagementBackend.model.Booking;
import com.example.GymManagementBackend.model.GymClass;
import com.example.GymManagementBackend.model.Role;
import com.example.GymManagementBackend.model.User;
import com.example.GymManagementBackend.model.Subscription;
import com.example.GymManagementBackend.repository.GymClassRepository;
import com.example.GymManagementBackend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GymClassServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private GymClassRepository gymClassRepository;

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private GymClassService gymClassService;

    private User user;
    private User trainer;
    private GymClass gymClass;
    private GymClassRequest gymClassRequest;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        Subscription activeSubscription = new Subscription();
        activeSubscription.setActive(true);
        user.setSubscription(activeSubscription);

        trainer = new User();
        trainer.setId(2L);
        trainer.setRole(Role.STAFF);

        gymClass = new GymClass();
        gymClass.setId(1L);

        gymClassRequest = new GymClassRequest("Yoga", 2L, LocalDateTime.now(), 60, 20);
    }

    @Test
    void createGymClass_shouldCreateSuccessfully() {
        when(userRepository.findById(2L)).thenReturn(Optional.of(trainer));
        when(gymClassRepository.save(any(GymClass.class))).thenAnswer(invocation -> invocation.getArgument(0));

        GymClass result = gymClassService.createGymClass(gymClassRequest);

        assertNotNull(result);
        assertEquals(gymClassRequest.name(), result.getName());
        assertEquals(trainer, result.getTrainer());
    }

    @Test
    void createGymClass_shouldThrowExceptionWhenTrainerNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            gymClassService.createGymClass(gymClassRequest);
        });
    }

    @Test
    void createGymClass_shouldThrowExceptionWhenUserIsNotStaff() {
        trainer.setRole(Role.MEMBER);
        when(userRepository.findById(2L)).thenReturn(Optional.of(trainer));

        assertThrows(ForbiddenException.class, () -> {
            gymClassService.createGymClass(gymClassRequest);
        });
    }

    @Test
    void createGymClass_shouldThrowExceptionWhenSchedulingConflict() {
        LocalDateTime now = LocalDateTime.now();
        GymClass existingClass = new GymClass();
        existingClass.setSchedule(now);
        existingClass.setDurationInMinutes(60);

        when(userRepository.findById(2L)).thenReturn(Optional.of(trainer));
        when(gymClassRepository.findByTrainer(trainer)).thenReturn(List.of(existingClass));

        GymClassRequest conflictingRequest = new GymClassRequest("Pilates", 2L, now.plusMinutes(30), 60, 15);

        assertThrows(SchedulingConflictException.class, () -> {
            gymClassService.createGymClass(conflictingRequest);
        });
    }

    @Test
    void bookClass_shouldBookSuccessfully() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(gymClassRepository.findById(1L)).thenReturn(Optional.of(gymClass));
        when(bookingService.createBooking(user, gymClass)).thenReturn(new Booking());

        Booking result = gymClassService.bookClass(1L, 1L);

        assertNotNull(result);
    }

    @Test
    void bookClass_shouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            gymClassService.bookClass(1L, 1L);
        });
    }

    @Test
    void bookClass_shouldThrowExceptionWhenGymClassNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(gymClassRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            gymClassService.bookClass(1L, 1L);
        });
    }

    @Test
    void bookClass_shouldThrowExceptionWhenSubscriptionInactive() {
        user.getSubscription().setActive(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(gymClassRepository.findById(1L)).thenReturn(Optional.of(gymClass));

        assertThrows(MembershipExpiredException.class, () -> {
            gymClassService.bookClass(1L, 1L);
        });
    }

    @Test
    void bookClass_shouldThrowExceptionWhenSubscriptionIsNull() {
        user.setSubscription(null);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(gymClassRepository.findById(1L)).thenReturn(Optional.of(gymClass));

        assertThrows(MembershipExpiredException.class, () -> {
            gymClassService.bookClass(1L, 1L);
        });
    }
}