package com.example.GymManagementBackend.controller;

import com.example.GymManagementBackend.model.User;
import com.example.GymManagementBackend.model.Role; // Import the Role enum
import com.example.GymManagementBackend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private User user;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        user = new User();
        user.setId(1L);
        user.setEmail("test.user@example.com");
        user.setRole(Role.MEMBER); // <-- FIX: Set the role here
    }

    @Test
    void getUserById_shouldReturnOk() throws Exception {
        when(userService.findUserById(anyLong())).thenReturn(user);

        mockMvc.perform(get("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getAllUsers_shouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/users/all"))
                .andExpect(status().isOk());
    }
}