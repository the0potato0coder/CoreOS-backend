package com.example.GymManagementBackend.repository;

import com.example.GymManagementBackend.model.Payment;
import com.example.GymManagementBackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    /**
     * Finds all payments made by a specific user.
     *
     * @param user The user whose payment history is required.
     * @return A list of the user's payments.
     */
    List<Payment> findByUser(User user);
}
