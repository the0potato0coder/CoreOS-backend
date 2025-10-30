package com.example.GymManagementBackend.controller;

import com.example.GymManagementBackend.dto.MembershipPlanRequest;
import com.example.GymManagementBackend.model.MembershipPlan;
import com.example.GymManagementBackend.service.MembershipService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MembershipController {

    @Autowired
    private MembershipService membershipService;

    /**
     * Creates a new membership plan.
     * This endpoint is for administrators only.
     * @param request The DTO containing the details for the new membership plan.
     * @return A ResponseEntity with the newly created MembershipPlan.
     */
    @PostMapping("/admin/membership-plans")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MembershipPlan> createMembershipPlan(@Valid @RequestBody MembershipPlanRequest request) {
        MembershipPlan newPlan = membershipService.createMembershipPlan(request);
        return new ResponseEntity<>(newPlan, HttpStatus.CREATED);
    }

    /**
     * Retrieves all available membership plans.
     * This endpoint is accessible to any authenticated user.
     *
     * @return A ResponseEntity containing a list of all MembershipPlans.
     */
    @GetMapping("/membership-plans")
    public ResponseEntity<List<MembershipPlan>> getAllMembershipPlans() {
        List<MembershipPlan> plans = membershipService.getAllMembershipPlans();
        return ResponseEntity.ok(plans);
    }
}
