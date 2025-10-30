package com.example.GymManagementBackend.service;

import com.example.GymManagementBackend.dto.MembershipPlanRequest;
import com.example.GymManagementBackend.exception.DuplicateResourceException;
import com.example.GymManagementBackend.exception.InvalidInputException;
import com.example.GymManagementBackend.model.MembershipPlan;
import com.example.GymManagementBackend.repository.MembershipPlanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MembershipServiceTest {

    @Mock
    private MembershipPlanRepository planRepository;

    @InjectMocks
    private MembershipService membershipService;

    private MembershipPlanRequest membershipPlanRequest;

    @BeforeEach
    void setUp() {
        membershipPlanRequest = new MembershipPlanRequest(
                "Gold",
                "Gold Plan",
                "All features included",
                50.0,
                30
        );
    }

    @Test
    void createMembershipPlan_shouldCreateSuccessfully() {
        when(planRepository.findByName(anyString())).thenReturn(Optional.empty());
        when(planRepository.save(any(MembershipPlan.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MembershipPlan result = membershipService.createMembershipPlan(membershipPlanRequest);

        assertNotNull(result);
        assertEquals(membershipPlanRequest.name(), result.getName());
        assertEquals(membershipPlanRequest.description(), result.getDescription());
        assertEquals(membershipPlanRequest.features(), result.getFeatures());
        assertEquals(membershipPlanRequest.price(), result.getPrice());
        assertEquals(membershipPlanRequest.durationInDays(), result.getDurationInDays());
    }

    @Test
    void createMembershipPlan_shouldThrowExceptionWhenNameIsEmpty() {
        MembershipPlanRequest invalidRequest = new MembershipPlanRequest(
                "",
                "Gold Plan",
                "All features included",
                50.0,
                30
        );

        assertThrows(InvalidInputException.class, () -> {
            membershipService.createMembershipPlan(invalidRequest);
        });
    }

    @Test
    void createMembershipPlan_shouldThrowExceptionWhenPriceIsZeroOrNegative() {
        MembershipPlanRequest invalidRequest = new MembershipPlanRequest(
                "Silver",
                "Silver Plan",
                "Some features",
                0.0,
                30
        );

        assertThrows(InvalidInputException.class, () -> {
            membershipService.createMembershipPlan(invalidRequest);
        });

        MembershipPlanRequest negativePriceRequest = new MembershipPlanRequest(
                "Bronze",
                "Bronze Plan",
                "Basic features",
                -10.0,
                30
        );

        assertThrows(InvalidInputException.class, () -> {
            membershipService.createMembershipPlan(negativePriceRequest);
        });
    }

    @Test
    void createMembershipPlan_shouldThrowExceptionWhenDurationIsZeroOrNegative() {
        MembershipPlanRequest invalidRequest = new MembershipPlanRequest(
                "Silver",
                "Silver Plan",
                "Some features",
                50.0,
                0
        );

        assertThrows(InvalidInputException.class, () -> {
            membershipService.createMembershipPlan(invalidRequest);
        });

        MembershipPlanRequest negativeDurationRequest = new MembershipPlanRequest(
                "Bronze",
                "Bronze Plan",
                "Basic features",
                50.0,
                -10
        );

        assertThrows(InvalidInputException.class, () -> {
            membershipService.createMembershipPlan(negativeDurationRequest);
        });
    }

    @Test
    void createMembershipPlan_shouldThrowExceptionWhenDuplicateNameExists() {
        when(planRepository.findByName(anyString())).thenReturn(Optional.of(new MembershipPlan()));

        assertThrows(DuplicateResourceException.class, () -> {
            membershipService.createMembershipPlan(membershipPlanRequest);
        });
    }
}