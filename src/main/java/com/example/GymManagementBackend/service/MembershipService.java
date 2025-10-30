package com.example.GymManagementBackend.service;

import com.example.GymManagementBackend.dto.MembershipPlanRequest;

import com.example.GymManagementBackend.exception.DuplicateResourceException;
import com.example.GymManagementBackend.exception.InvalidInputException;
import com.example.GymManagementBackend.model.MembershipPlan;

import com.example.GymManagementBackend.repository.MembershipPlanRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class MembershipService {

    private final MembershipPlanRepository planRepository;

    /**
     * Creates a new membership plan.
     * This method is intended to be called from a protected endpoint accessible only to ADMINs.
     * @param request The MembershipPlanRequest DTO containing plan details.
     * @return The newly created MembershipPlan entity.
     */
    @Transactional
    public MembershipPlan createMembershipPlan(MembershipPlanRequest request) {
        // 1. Validate input
        if (request.name() == null || request.name().isBlank()) {
            throw new InvalidInputException("Membership plan name cannot be empty.");
        }
        if (request.price() <= 0) {
            throw new InvalidInputException("Price must be positive.");
        }
        if (request.durationInDays() <= 0) {
            throw new InvalidInputException("Duration must be positive.");
        }

        // 2. Check for duplicate plan name
        if (planRepository.findByName(request.name()).isPresent()) {
            throw new DuplicateResourceException("A membership plan with the name '" + request.name() + "' already exists.");
        }

        MembershipPlan newPlan = new MembershipPlan();
        newPlan.setName(request.name());
        newPlan.setDescription(request.description());
        newPlan.setFeatures(request.features()); // Setting the new features field
        newPlan.setPrice(request.price());
        newPlan.setDurationInDays(request.durationInDays());

        return planRepository.save(newPlan);
    }

    /**
     * Retrieves all available membership plans.
     * @return A list of all MembershipPlan entities.
     */
    public List<MembershipPlan> getAllMembershipPlans() {
        return planRepository.findAll();
    }


}