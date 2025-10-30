package com.example.GymManagementBackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "gym_classes")
@Getter
@Setter
@NoArgsConstructor
public class GymClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    // FIX: Changed FetchType to EAGER
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "trainer_id", referencedColumnName = "id")
    private User trainer; // A user with the role STAFF

    @Column(nullable = false)
    private LocalDateTime schedule; // Start time and date of the class

    @Column(nullable = false)
    private Integer durationInMinutes;

    @Column(nullable = false)
    private Integer capacity;

    // --- Relationships ---
    @OneToMany(mappedBy = "gymClass", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Booking> bookings;
}