package com.example.GymManagementBackend.service;

import com.example.GymManagementBackend.dto.UserRegistrationRequest;
import com.example.GymManagementBackend.exception.DuplicateResourceException;
import com.example.GymManagementBackend.model.Role;
import com.example.GymManagementBackend.model.User;
import com.example.GymManagementBackend.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User registerMember(@NotNull UserRegistrationRequest request) {
        // 1. Check if email already exists
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new DuplicateResourceException("Email address is already in use.");
        }

        // 2. Create and configure the new user entity
        User newUser = new User();
        newUser.setFirstName(request.firstName());
        newUser.setLastName(request.lastName());
        newUser.setEmail(request.email());
        newUser.setPassword(passwordEncoder.encode(request.password())); // 3. Hash the password
        newUser.setDateOfBirth(request.dateOfBirth());
        newUser.setRole(Role.MEMBER); // 4. Default role for registration is MEMBER

        // 5. Save and return the new user
        return userRepository.save(newUser);
    }

    /**
     * Registers a new user with the ADMIN role.
     * This method is called from a protected endpoint accessible only to existing ADMINs.
     * @param request The UserRegistrationRequest containing user details.
     * @return The newly created User entity with the ADMIN role.
     */
    @Transactional
    public User registerAdmin(@NotNull UserRegistrationRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new DuplicateResourceException("Email address is already in use.");
        }

        User newUser = new User();
        newUser.setFirstName(request.firstName());
        newUser.setLastName(request.lastName());
        newUser.setEmail(request.email());
        newUser.setPassword(passwordEncoder.encode(request.password()));
        newUser.setDateOfBirth(request.dateOfBirth());
        newUser.setRole(Role.ADMIN); // Set the role to ADMIN

        return userRepository.save(newUser);
    }

    /**
     * Registers a new user with the STAFF role.
     * This method is called from a protected endpoint accessible only to existing ADMINs.
     * @param request The UserRegistrationRequest containing user details.
     * @return The newly created User entity with the STAFF role.
     */
    @Transactional
    public User registerStaff(@NotNull UserRegistrationRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new DuplicateResourceException("Email address is already in use.");
        }

        User newUser = new User();
        newUser.setFirstName(request.firstName());
        newUser.setLastName(request.lastName());
        newUser.setEmail(request.email());
        newUser.setPassword(passwordEncoder.encode(request.password()));
        newUser.setDateOfBirth(request.dateOfBirth());
        newUser.setRole(Role.STAFF); // Set the role to STAFF

        return userRepository.save(newUser);
    }


}
