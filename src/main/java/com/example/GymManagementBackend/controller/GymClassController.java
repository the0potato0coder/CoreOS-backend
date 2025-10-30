package com.example.GymManagementBackend.controller;

import com.example.GymManagementBackend.dto.GymClassRequest;
import com.example.GymManagementBackend.model.GymClass;
import com.example.GymManagementBackend.service.GymClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class GymClassController {

    private final GymClassService gymClassService;


    /**
     * Creates a new gym class.
     * This endpoint is for administrators and staff.
     * - If called by an ADMIN, a trainerId must be provided in the request body.
     * - If called by a STAFF member, the trainerId should be omitted, as they will be assigned as the trainer.
     *
     * @param request The DTO containing the details for the new gym class.
     * @return A ResponseEntity with the newly created GymClass.
     */
    @PostMapping("/admin/gym-classes")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<GymClass> createGymClass(@Valid @RequestBody GymClassRequest request) {
        GymClass newClass = gymClassService.createGymClass(request);
        return new ResponseEntity<>(newClass, HttpStatus.CREATED);
    }

    /**
     * Retrieves all available gym classes.
     * This endpoint is accessible to any authenticated user.
     *
     * @return A ResponseEntity containing a list of all GymClasses.
     */
    @GetMapping("/gym-classes")
    public ResponseEntity<List<GymClass>> getAllGymClasses() {
        List<GymClass> classes = gymClassService.getAllGymClasses();
        return ResponseEntity.ok(classes);
    }
}
