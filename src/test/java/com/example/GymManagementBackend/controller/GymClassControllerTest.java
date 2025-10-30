package com.example.GymManagementBackend.controller;

import com.example.GymManagementBackend.dto.GymClassRequest;
import com.example.GymManagementBackend.model.GymClass;
import com.example.GymManagementBackend.service.GymClassService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class GymClassControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GymClassService gymClassService;

    @InjectMocks
    private GymClassController gymClassController;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private GymClassRequest gymClassRequest;
    private GymClass gymClass;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(gymClassController).build();
        gymClassRequest = new GymClassRequest(
                "Yoga",
                1L,
                LocalDateTime.now(),
                60,
                20
        );
        gymClass = new GymClass();
        gymClass.setId(1L);
        gymClass.setName("Yoga");
    }

    @Test
    void createGymClass_shouldReturnCreated() throws Exception {
        when(gymClassService.createGymClass(any(GymClassRequest.class))).thenReturn(gymClass);

        mockMvc.perform(post("/api/admin/gym-classes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(gymClassRequest)))
                .andExpect(status().isCreated());
    }
}