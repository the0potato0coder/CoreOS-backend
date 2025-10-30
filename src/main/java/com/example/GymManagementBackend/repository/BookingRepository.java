package com.example.GymManagementBackend.repository;

import com.example.GymManagementBackend.model.Booking;
import com.example.GymManagementBackend.model.User;
import com.example.GymManagementBackend.model.GymClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    // This method is used to check if a user has already booked a specific class.
    Optional<Booking> findByUserAndGymClass(User user, GymClass gymClass);
}
