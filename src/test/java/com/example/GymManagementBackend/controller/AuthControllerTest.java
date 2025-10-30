package com.example.GymManagementBackend.controller;

import com.example.GymManagementBackend.dto.UserRegistrationRequest;
import com.example.GymManagementBackend.model.User;
import com.example.GymManagementBackend.model.Role; // Import the Role enum
import com.example.GymManagementBackend.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private UserRegistrationRequest userRegistrationRequest;
    private User user;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        userRegistrationRequest = new UserRegistrationRequest(
                "John",
                "Doe",
                "john.doe@example.com",
                "password",
                LocalDate.now()
        );
        user = new User();
        user.setId(1L);
        user.setEmail("john.doe@example.com");
        user.setRole(Role.MEMBER); // <-- FIX: Set the role here
    }

    @Test
    void registerMember_shouldReturnCreated() throws Exception {
        when(authService.registerMember(any(UserRegistrationRequest.class))).thenReturn(user);

        mockMvc.perform(post("/api/auth/register/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRegistrationRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    void registerMember_shouldReturnBadRequest_whenEmailAlreadyExists() throws Exception {
        when(authService.registerMember(any(UserRegistrationRequest.class)))
                .thenThrow(new IllegalArgumentException("Email already exists"));

        mockMvc.perform(post("/api/auth/register/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRegistrationRequest)))
                .andExpect(status().isBadRequest());
    }

}