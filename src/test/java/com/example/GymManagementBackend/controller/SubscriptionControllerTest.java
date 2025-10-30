package com.example.GymManagementBackend.controller;

import com.example.GymManagementBackend.dto.SubscriptionRequest;
import com.example.GymManagementBackend.model.Subscription;
import com.example.GymManagementBackend.service.SubscriptionService;
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
public class SubscriptionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SubscriptionService subscriptionService;

    @InjectMocks
    private SubscriptionController subscriptionController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private SubscriptionRequest subscriptionRequest;
    private Subscription subscription;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(subscriptionController).build();
        subscriptionRequest = new SubscriptionRequest(1L, 1L);
        subscription = new Subscription();
        subscription.setId(1L);
    }

    @Test
    void createSubscription_shouldReturnCreated() throws Exception {
        when(subscriptionService.createSubscription(any(SubscriptionRequest.class))).thenReturn(subscription);

        mockMvc.perform(post("/api/members/subscribe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subscriptionRequest)))
                .andExpect(status().isCreated());
    }
}