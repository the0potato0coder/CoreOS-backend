package com.example.GymManagementBackend.repository;

import com.example.GymManagementBackend.model.GymClass;
import com.example.GymManagementBackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GymClassRepository extends JpaRepository<GymClass, Long> {
    List<GymClass> findByTrainer(User trainer);
    // Standard CRUD methods are sufficient for now.
}
