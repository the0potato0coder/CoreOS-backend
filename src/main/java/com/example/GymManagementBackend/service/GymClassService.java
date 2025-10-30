package com.example.GymManagementBackend.service;

import com.example.GymManagementBackend.exception.ForbiddenException;
import com.example.GymManagementBackend.exception.MembershipExpiredException;
import com.example.GymManagementBackend.exception.ResourceNotFoundException;
import com.example.GymManagementBackend.exception.SchedulingConflictException;
import com.example.GymManagementBackend.model.GymClass;
import com.example.GymManagementBackend.model.User;
import com.example.GymManagementBackend.model.Booking;
import com.example.GymManagementBackend.repository.GymClassRepository;
import com.example.GymManagementBackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.GymManagementBackend.dto.GymClassRequest;
import com.example.GymManagementBackend.model.Role;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GymClassService {

    private final UserRepository userRepository;
    private final GymClassRepository gymClassRepository;
    private final BookingService bookingService; // Injected the new BookingService


    /**
     * Allows a user to book a gym class.
     * This method now uses the BookingService to handle the booking creation logic.
     *
     * @param userId The ID of the user attempting to book.
     * @param classId The ID of the class to be booked.
     * @return The newly created Booking entity.
     */
    @Transactional
    public Booking bookClass(Long userId, Long classId) {
        // Step 1: Find the user and gym class
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        GymClass gymClass = gymClassRepository.findById(classId)
                .orElseThrow(() -> new ResourceNotFoundException("Gym class not found with id: " + classId));

        // Step 2: Implement membership check logic
        if (user.getSubscription() == null || !user.getSubscription().isActive()) {
            throw new MembershipExpiredException("User must have an active membership to book a class.");
        }

        // Step 3: Delegate the booking creation to the new BookingService
        return bookingService.createBooking(user, gymClass);
    }

    /**
     * Schedules a new gym class.
     * This method is intended to be called by an ADMIN. It performs validation
     * to ensure the trainer exists and has the 'STAFF' role.
     *
     * @param request The DTO containing the details for the new gym class.
     * @return The newly created GymClass entity.
     */
    @Transactional
    public GymClass createGymClass(GymClassRequest request) {
        // 1. Find the trainer and validate their role
        User trainer = userRepository.findById(request.trainerId())
                .orElseThrow(() -> new ResourceNotFoundException("Trainer not found with id: " + request.trainerId()));

        if (trainer.getRole() != Role.STAFF) {
            throw new ForbiddenException("The specified user is not a STAFF and cannot be a trainer.");
        }

        // 2. Check for scheduling conflicts for the trainer
        LocalDateTime newClassStartTime = request.schedule();
        LocalDateTime newClassEndTime = newClassStartTime.plusMinutes(request.durationInMinutes());

        List<GymClass> trainerClasses = gymClassRepository.findByTrainer(trainer);
        for (GymClass existingClass : trainerClasses) {
            LocalDateTime existingClassStartTime = existingClass.getSchedule();
            LocalDateTime existingClassEndTime = existingClassStartTime.plusMinutes(existingClass.getDurationInMinutes());

            // Check for overlap
            if (newClassStartTime.isBefore(existingClassEndTime) && newClassEndTime.isAfter(existingClassStartTime)) {
                throw new SchedulingConflictException("Trainer is already scheduled for another class during this time: " + existingClass.getName());
            }
        }

        // 3. Create the new GymClass entity
        GymClass newClass = new GymClass();
        newClass.setName(request.name());
        newClass.setTrainer(trainer);
        newClass.setSchedule(request.schedule());
        newClass.setDurationInMinutes(request.durationInMinutes());
        newClass.setCapacity(request.capacity());

        // 4. Save the new class to the database
        return gymClassRepository.save(newClass);
    }

    /**
     * Retrieves all available gym classes.
     * @return A list of all GymClass entities.
     */
    public List<GymClass> getAllGymClasses() {
        return gymClassRepository.findAll();
    }
}