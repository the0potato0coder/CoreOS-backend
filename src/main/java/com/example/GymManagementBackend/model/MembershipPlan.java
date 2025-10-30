package com.example.GymManagementBackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore; // <-- ADD THIS IMPORT
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Set;

@Entity
@Table(name = "membership_plans")
@Getter
@Setter
@NoArgsConstructor
public class MembershipPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String features;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer durationInDays;

    @JsonIgnore // <-- ADD THIS ANNOTATION
    @OneToMany(mappedBy = "membershipPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Subscription> subscriptions;
}