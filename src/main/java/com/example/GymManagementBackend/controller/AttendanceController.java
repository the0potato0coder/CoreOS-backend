// From GymManagementBackend/src/main/java/com/example/GymManagementBackend/controller/AttendanceController.java

package com.example.GymManagementBackend.controller;

import com.example.GymManagementBackend.model.Attendance;
import com.example.GymManagementBackend.model.User;
import com.example.GymManagementBackend.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping("/check-in")
    @PreAuthorize("hasAnyRole('MEMBER', 'STAFF')")
    public ResponseEntity<Attendance> markAttendance(@AuthenticationPrincipal User user) {
        Attendance newAttendance = attendanceService.markAttendance(user);
        return new ResponseEntity<>(newAttendance, HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("#userId == authentication.principal.id or hasRole('ADMIN')")
    public ResponseEntity<List<Attendance>> getAttendanceHistory(@PathVariable Long userId) {
        List<Attendance> attendanceHistory = attendanceService.getAttendanceHistory(userId);
        return ResponseEntity.ok(attendanceHistory);
    }
}