package com.example.GymManagementBackend.service;

import com.example.GymManagementBackend.dto.SubscriptionRequest;
import com.example.GymManagementBackend.exception.DuplicateResourceException;
import com.example.GymManagementBackend.exception.ForbiddenException;
import com.example.GymManagementBackend.exception.ResourceNotFoundException;
import com.example.GymManagementBackend.model.MembershipPlan;
import com.example.GymManagementBackend.model.Subscription;
import com.example.GymManagementBackend.model.User;
import com.example.GymManagementBackend.repository.MembershipPlanRepository;
import com.example.GymManagementBackend.repository.SubscriptionRepository;
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
public class SubscriptionServiceTest {

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MembershipPlanRepository planRepository;

    @InjectMocks
    private SubscriptionService subscriptionService;

    private User user;
    private MembershipPlan plan;
    private SubscriptionRequest subscriptionRequest;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setDummyTokens(100.0);

        plan = new MembershipPlan();
        plan.setId(1L);
        plan.setPrice(50.0);
        plan.setDurationInDays(30);

        subscriptionRequest = new SubscriptionRequest(1L, 1L);
    }

    @Test
    void createSubscription_shouldSucceed() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(planRepository.findById(anyLong())).thenReturn(Optional.of(plan));
        when(subscriptionRepository.findByUserAndActiveTrue(any(User.class))).thenReturn(Optional.empty());
        when(subscriptionRepository.save(any(Subscription.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Subscription result = subscriptionService.createSubscription(subscriptionRequest);

        assertNotNull(result);
        assertTrue(result.isActive());
        assertEquals(50.0, user.getDummyTokens());
    }

    @Test
    void createSubscription_shouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            subscriptionService.createSubscription(subscriptionRequest);
        });
    }

    @Test
    void createSubscription_shouldThrowExceptionWhenPlanNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(planRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            subscriptionService.createSubscription(subscriptionRequest);
        });
    }

    @Test
    void createSubscription_shouldThrowExceptionWhenAlreadySubscribed() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(planRepository.findById(anyLong())).thenReturn(Optional.of(plan));
        when(subscriptionRepository.findByUserAndActiveTrue(any(User.class))).thenReturn(Optional.of(new Subscription()));

        assertThrows(DuplicateResourceException.class, () -> {
            subscriptionService.createSubscription(subscriptionRequest);
        });
    }

    @Test
    void createSubscription_shouldThrowExceptionWhenInsufficientTokens() {
        user.setDummyTokens(20.0);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(planRepository.findById(anyLong())).thenReturn(Optional.of(plan));
        when(subscriptionRepository.findByUserAndActiveTrue(any(User.class))).thenReturn(Optional.empty());

        assertThrows(ForbiddenException.class, () -> {
            subscriptionService.createSubscription(subscriptionRequest);
        });
    }
}