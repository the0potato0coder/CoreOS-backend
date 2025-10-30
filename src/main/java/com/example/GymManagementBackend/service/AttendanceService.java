package com.example.GymManagementBackend.service;

import com.example.GymManagementBackend.exception.MembershipExpiredException;
import com.example.GymManagementBackend.exception.ResourceNotFoundException;
import com.example.GymManagementBackend.model.Attendance;
import com.example.GymManagementBackend.model.User;
import com.example.GymManagementBackend.repository.AttendanceRepository;
import com.example.GymManagementBackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;

    @Transactional
    public Attendance markAttendance(User user) {
        if (user.getSubscription() == null || !user.getSubscription().isActive()) {
            throw new MembershipExpiredException("User does not have an active membership to mark attendance.");
        }

        Attendance attendance = new Attendance();
        attendance.setUser(user);

        return attendanceRepository.save(attendance);
    }

    public List<Attendance> getAttendanceHistory(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        return attendanceRepository.findByUserOrderByCheckInTimeDesc(user);
    }
}