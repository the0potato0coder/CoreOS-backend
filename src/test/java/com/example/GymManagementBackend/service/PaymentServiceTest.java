package com.example.GymManagementBackend.service;

import com.example.GymManagementBackend.dto.PaymentRequest;
import com.example.GymManagementBackend.exception.InvalidInputException;
import com.example.GymManagementBackend.exception.ResourceNotFoundException;
import com.example.GymManagementBackend.model.Payment;
import com.example.GymManagementBackend.model.User;
import com.example.GymManagementBackend.repository.PaymentRepository;
import com.example.GymManagementBackend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PaymentService paymentService;

    private User user;
    private PaymentRequest paymentRequest;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setDummyTokens(100.0);

        paymentRequest = new PaymentRequest(1L, 50.0);
    }

    @Test
    void makeDummyPayment_shouldSucceed() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment result = paymentService.makeDummyPayment(paymentRequest);

        assertNotNull(result);
        assertEquals(user, result.getUser());
        assertEquals(paymentRequest.amount(), result.getAmount());
        assertEquals(150.0, user.getDummyTokens());
    }

    @Test
    void makeDummyPayment_shouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            paymentService.makeDummyPayment(paymentRequest);
        });
    }

    @Test
    void makeDummyPayment_shouldThrowExceptionForInvalidAmount() {
        PaymentRequest invalidRequest = new PaymentRequest(1L, -50.0);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        assertThrows(InvalidInputException.class, () -> {
            paymentService.makeDummyPayment(invalidRequest);
        });
    }
}