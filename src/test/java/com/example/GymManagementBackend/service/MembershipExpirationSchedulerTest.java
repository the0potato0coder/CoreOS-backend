package com.example.GymManagementBackend.service;

import com.example.GymManagementBackend.model.Subscription;
import com.example.GymManagementBackend.model.User;
import com.example.GymManagementBackend.repository.SubscriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MembershipExpirationSchedulerTest {

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @InjectMocks
    private MembershipExpirationScheduler scheduler;

    private Subscription expiredSubscription;
    private Subscription activeSubscription;

    @BeforeEach
    void setUp() {
        User user1 = new User();
        user1.setEmail("expired@example.com");

        expiredSubscription = new Subscription();
        expiredSubscription.setUser(user1);
        expiredSubscription.setActive(true);
        expiredSubscription.setEndDate(LocalDate.now().minusDays(1)); // Expired yesterday

        User user2 = new User();
        user2.setEmail("active@example.com");

        activeSubscription = new Subscription();
        activeSubscription.setUser(user2);
        activeSubscription.setActive(true);
        activeSubscription.setEndDate(LocalDate.now().plusDays(10)); // Expires in 10 days
    }

    @Test
    void expireOldMemberships_shouldDeactivateExpiredSubscriptions() {
        when(subscriptionRepository.findByActiveTrue()).thenReturn(List.of(expiredSubscription, activeSubscription));

        scheduler.expireOldMemberships();

        // Verify that save was called only on the expired subscription
        verify(subscriptionRepository, times(1)).save(expiredSubscription);
        verify(subscriptionRepository, never()).save(activeSubscription);

        // Assert that the expired subscription is now inactive
        assertFalse(expiredSubscription.isActive());
        // Assert that the active subscription remains active
        assertTrue(activeSubscription.isActive());
    }

    @Test
    void expireOldMemberships_shouldDoNothingWhenNoActiveSubscriptions() {
        when(subscriptionRepository.findByActiveTrue()).thenReturn(Collections.emptyList());

        scheduler.expireOldMemberships();

        // Verify that save is never called
        verify(subscriptionRepository, never()).save(any(Subscription.class));
    }

    @Test
    void expireOldMemberships_shouldHandleSubscriptionsWithNullEndDate() {
        activeSubscription.setEndDate(null); // A subscription that never expires
        when(subscriptionRepository.findByActiveTrue()).thenReturn(List.of(activeSubscription));

        scheduler.expireOldMemberships();

        // Verify that save is never called
        verify(subscriptionRepository, never()).save(any(Subscription.class));
        assertTrue(activeSubscription.isActive());
    }
}