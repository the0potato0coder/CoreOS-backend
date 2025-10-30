package com.example.GymManagementBackend.controller;

import com.example.GymManagementBackend.model.Attendance;
import com.example.GymManagementBackend.model.User;
import com.example.GymManagementBackend.model.Role;
import com.example.GymManagementBackend.service.AttendanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AttendanceControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AttendanceService attendanceService;

    @InjectMocks
    private AttendanceController attendanceController;

    private User user;
    private Attendance attendance;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(attendanceController).build();
        user = new User();
        user.setId(1L);
        user.setEmail("test.user@example.com");
        user.setRole(Role.MEMBER);

        attendance = new Attendance();
        attendance.setId(1L);
        attendance.setUser(user);

        // Manually set up the SecurityContext
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities())
        );
    }

    @Test
    void markAttendance_shouldReturnCreated() throws Exception {
        when(attendanceService.markAttendance(any(User.class))).thenReturn(attendance);

        mockMvc.perform(post("/api/attendance/check-in")
                        .principal(() -> "test.user@example.com")) // Simulate authentication
                .andExpect(status().isCreated());
    }

    @Test
    void getAttendanceHistory_shouldReturnOk() throws Exception {
        when(attendanceService.getAttendanceHistory(anyLong())).thenReturn(Collections.singletonList(attendance));

        mockMvc.perform(get("/api/attendance/1")
                        .principal(() -> "test.user@example.com") // Simulate authentication
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}