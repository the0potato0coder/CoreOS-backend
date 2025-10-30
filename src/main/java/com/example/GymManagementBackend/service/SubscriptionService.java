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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final MembershipPlanRepository planRepository;

    /**
     * Creates a new subscription for a user, deducting the cost from their token balance.
     * @param request The DTO containing the user ID and membership plan ID.
     * @return The newly created Subscription entity.
     */
    @Transactional
    public Subscription createSubscription(SubscriptionRequest request) {
        // Step 1: Find the user and membership plan
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.userId()));

        MembershipPlan plan = planRepository.findById(request.planId())
                .orElseThrow(() -> new ResourceNotFoundException("Membership Plan not found with id: " + request.planId()));

        // Step 2: Business Rule - Check if the user already has an active subscription
        subscriptionRepository.findByUserAndActiveTrue(user).ifPresent(sub -> {
            throw new DuplicateResourceException("User already has an active subscription.");
        });

        // Step 3: Business Rule - Check if the user has enough dummy tokens to pay for the plan.
        if (user.getDummyTokens() < plan.getPrice()) {
            throw new ForbiddenException("Insufficient dummy tokens to purchase this membership.");
        }

        // Step 4: Process the "payment" by deducting tokens from the user's balance.
        user.setDummyTokens(user.getDummyTokens() - plan.getPrice());
        // Save the updated user with the new token balance.
        userRepository.save(user);

        // Step 5: Create the new subscription
        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setMembershipPlan(plan);
        subscription.setStartDate(LocalDate.now());
        subscription.setEndDate(LocalDate.now().plusDays(plan.getDurationInDays()));
        subscription.setActive(true);

        return subscriptionRepository.save(subscription);
    }
}
