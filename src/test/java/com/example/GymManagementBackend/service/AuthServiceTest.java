package com.example.GymManagementBackend.service;

import com.example.GymManagementBackend.dto.UserRegistrationRequest;
import com.example.GymManagementBackend.exception.DuplicateResourceException;
import com.example.GymManagementBackend.model.Role;
import com.example.GymManagementBackend.model.User;
import com.example.GymManagementBackend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private UserRegistrationRequest userRegistrationRequest;

    @BeforeEach
    void setUp() {
        userRegistrationRequest = new UserRegistrationRequest(
                "John",
                "Doe",
                "john.doe@example.com",
                "password",
                LocalDate.now()
        );
    }

    @Test
    void registerMember_shouldRegisterSuccessfully() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = authService.registerMember(userRegistrationRequest);

        assertNotNull(result);
        assertEquals(userRegistrationRequest.email(), result.getEmail());
        assertEquals(Role.MEMBER, result.getRole());
    }

    @Test
    void registerMember_shouldThrowExceptionWhenEmailExists() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new User()));

        assertThrows(DuplicateResourceException.class, () -> {
            authService.registerMember(userRegistrationRequest);
        });
    }

    @Test
    void registerAdmin_shouldRegisterSuccessfully() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = authService.registerAdmin(userRegistrationRequest);

        assertNotNull(result);
        assertEquals(userRegistrationRequest.email(), result.getEmail());
        assertEquals(Role.ADMIN, result.getRole());
    }

    @Test
    void registerAdmin_shouldThrowExceptionWhenEmailExists() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new User()));

        assertThrows(DuplicateResourceException.class, () -> {
            authService.registerAdmin(userRegistrationRequest);
        });
    }

    @Test
    void registerStaff_shouldRegisterSuccessfully() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = authService.registerStaff(userRegistrationRequest);

        assertNotNull(result);
        assertEquals(userRegistrationRequest.email(), result.getEmail());
        assertEquals(Role.STAFF, result.getRole());
    }

    @Test
    void registerStaff_shouldThrowExceptionWhenEmailExists() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new User()));

        assertThrows(DuplicateResourceException.class, () -> {
            authService.registerStaff(userRegistrationRequest);
        });
    }
}