package com.example.GymManagementBackend.service;

import com.example.GymManagementBackend.dto.PaymentRequest;
import com.example.GymManagementBackend.exception.InvalidInputException;
import com.example.GymManagementBackend.exception.ResourceNotFoundException;
import com.example.GymManagementBackend.model.Payment;
import com.example.GymManagementBackend.model.User;
import com.example.GymManagementBackend.repository.PaymentRepository;
import com.example.GymManagementBackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;

    /**
     * Creates a new dummy payment record and updates the user's dummy token balance.
     * This method is intended to be called by a MEMBER.
     *
     * @param request The DTO containing the user ID and payment amount.
     * @return The newly created Payment entity.
     */
    @Transactional
    public Payment makeDummyPayment(PaymentRequest request) {
        // 1. Find the user
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.userId()));

        // 2. Validate the payment amount
        if (request.amount() <= 0) {
            throw new InvalidInputException("Payment amount must be positive.");
        }

        // 3. Create the payment record
        Payment payment = new Payment();
        payment.setUser(user);
        payment.setAmount(request.amount());
        payment.setPaymentMethod("DUMMY_TOKEN");
        payment.setTransactionId(UUID.randomUUID().toString()); // Generate a unique transaction ID

        // 4. Save the payment
        Payment newPayment = paymentRepository.save(payment);

        // 5. Update the user's dummy tokens
        user.setDummyTokens(user.getDummyTokens() + request.amount());
        userRepository.save(user);

        return newPayment;
    }
}
