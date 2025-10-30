package com.example.GymManagementBackend.service;

import com.example.GymManagementBackend.exception.MembershipExpiredException;
import com.example.GymManagementBackend.exception.ResourceNotFoundException;
import com.example.GymManagementBackend.model.Attendance;
import com.example.GymManagementBackend.model.Subscription;
import com.example.GymManagementBackend.model.User;
import com.example.GymManagementBackend.repository.AttendanceRepository;
import com.example.GymManagementBackend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AttendanceServiceTest {

    @Mock
    private AttendanceRepository attendanceRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AttendanceService attendanceService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
    }

    @Test
    void markAttendance_shouldSaveAttendance() {
        Subscription activeSubscription = new Subscription();
        activeSubscription.setActive(true);
        user.setSubscription(activeSubscription);

        when(attendanceRepository.save(any(Attendance.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Attendance result = attendanceService.markAttendance(user);

        assertNotNull(result);
        assertEquals(user, result.getUser());
    }

    @Test
    void markAttendance_shouldThrowExceptionWhenSubscriptionInactive() {
        Subscription inactiveSubscription = new Subscription();
        inactiveSubscription.setActive(false);
        user.setSubscription(inactiveSubscription);

        assertThrows(MembershipExpiredException.class, () -> {
            attendanceService.markAttendance(user);
        });
    }

    @Test
    void markAttendance_shouldThrowExceptionWhenSubscriptionIsNull() {
        user.setSubscription(null);

        assertThrows(MembershipExpiredException.class, () -> {
            attendanceService.markAttendance(user);
        });
    }

    @Test
    void getAttendanceHistory_shouldReturnHistory() {
        Attendance attendance = new Attendance();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(attendanceRepository.findByUserOrderByCheckInTimeDesc(any(User.class))).thenReturn(Collections.singletonList(attendance));

        List<Attendance> result = attendanceService.getAttendanceHistory(1L);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void getAttendanceHistory_shouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            attendanceService.getAttendanceHistory(1L);
        });
    }
}