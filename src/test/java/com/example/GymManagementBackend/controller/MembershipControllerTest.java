package com.example.GymManagementBackend.controller;

import com.example.GymManagementBackend.dto.MembershipPlanRequest;
import com.example.GymManagementBackend.model.MembershipPlan;
import com.example.GymManagementBackend.service.MembershipService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class MembershipControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MembershipService membershipService;

    @InjectMocks
    private MembershipController membershipController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MembershipPlanRequest membershipPlanRequest;
    private MembershipPlan membershipPlan;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(membershipController).build();
        membershipPlanRequest = new MembershipPlanRequest(
                "Gold Plan",
                "All access",
                "All access",
                100.0,
                30
        );
        membershipPlan = new MembershipPlan();
        membershipPlan.setId(1L);
        membershipPlan.setName("Gold Plan");
    }

    @Test
    void createMembershipPlan_shouldReturnCreated() throws Exception {
        when(membershipService.createMembershipPlan(any(MembershipPlanRequest.class))).thenReturn(membershipPlan);

        mockMvc.perform(post("/api/admin/membership-plans")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(membershipPlanRequest)))
                .andExpect(status().isCreated());
    }
}