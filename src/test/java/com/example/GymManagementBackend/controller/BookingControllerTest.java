package com.example.GymManagementBackend.controller;

import com.example.GymManagementBackend.dto.BookingRequest;
import com.example.GymManagementBackend.model.Booking;
import com.example.GymManagementBackend.service.GymClassService;
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

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class BookingControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GymClassService gymClassService;

    @InjectMocks
    private BookingController bookingController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private BookingRequest bookingRequest;
    private Booking booking;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookingController).build();
        bookingRequest = new BookingRequest(1L, 1L);
        booking = new Booking();
        booking.setId(1L);
    }

    @Test
    void bookClass_shouldReturnCreated() throws Exception {
        when(gymClassService.bookClass(anyLong(), anyLong())).thenReturn(booking);

        mockMvc.perform(post("/api/members/book-class")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingRequest)))
                .andExpect(status().isCreated());
    }
}