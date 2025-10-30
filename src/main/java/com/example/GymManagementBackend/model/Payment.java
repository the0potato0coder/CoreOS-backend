package com.example.GymManagementBackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false, updatable = false)
    private final LocalDateTime paymentDate = LocalDateTime.now();

    @Column(length = 50)
    private String paymentMethod; // e.g., "DUMMY_TOKEN"

    @Column(length = 100)
    private String transactionId; // Dummy transaction ID for now

    // --- Relationships ---

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
