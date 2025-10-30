package com.example.GymManagementBackend.controller;

import com.example.GymManagementBackend.dto.PaymentRequest;
import com.example.GymManagementBackend.model.Payment;
import com.example.GymManagementBackend.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * Allows a member to make a dummy payment to acquire dummy tokens.
     * This endpoint is for members only.
     *
     * @param request The DTO containing the user ID and the amount to pay.
     * @return A ResponseEntity with the newly created Payment.
     */
    @PostMapping("/members/payments")
    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<Payment> makeDummyPayment(@Valid @RequestBody PaymentRequest request) {
        Payment newPayment = paymentService.makeDummyPayment(request);
        return new ResponseEntity<>(newPayment, HttpStatus.CREATED);
    }
}
