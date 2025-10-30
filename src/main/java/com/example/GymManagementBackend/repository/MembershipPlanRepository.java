package com.example.GymManagementBackend.repository;

import com.example.GymManagementBackend.model.MembershipPlan;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MembershipPlanRepository extends JpaRepository<MembershipPlan, Long> {
    Optional<Object> findByName(@NotBlank @Size(max = 100) String name);
    // Standard CRUD methods are sufficient for now.
}