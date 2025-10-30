package com.example.GymManagementBackend.repository;

import com.example.GymManagementBackend.model.Attendance;
import com.example.GymManagementBackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    /**
     * Finds all attendance records for a specific user, ordered by check-in time descending.
     *
     * @param user The user whose attendance history is required.
     * @return A list of attendance records.
     */
    List<Attendance> findByUserOrderByCheckInTimeDesc(User user);
}