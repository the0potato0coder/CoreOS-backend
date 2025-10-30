package com.example.GymManagementBackend.controller;

import com.example.GymManagementBackend.dto.SubscriptionRequest;
import com.example.GymManagementBackend.model.Subscription;
import com.example.GymManagementBackend.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    /**
     * Allows a member to subscribe to a membership plan.
     * This endpoint is for members only.
     * @param request The DTO containing the user ID and the membership plan ID.
     * @return A ResponseEntity with the newly created Subscription entity.
     */
    @PostMapping("/members/subscribe")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<Subscription> createSubscription(@Valid @RequestBody SubscriptionRequest request) {
        Subscription newSubscription = subscriptionService.createSubscription(request);
        return new ResponseEntity<>(newSubscription, HttpStatus.CREATED);
    }
}
