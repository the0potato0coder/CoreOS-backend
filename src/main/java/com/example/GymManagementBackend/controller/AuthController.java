// From GymManagementBackend/src/main/java/com/example/GymManagementBackend/controller/AuthController.java

package com.example.GymManagementBackend.controller;

import com.example.GymManagementBackend.dto.UserRegistrationRequest;
import com.example.GymManagementBackend.model.User;
import com.example.GymManagementBackend.security.JwtService;
import com.example.GymManagementBackend.service.AuthService;
import com.example.GymManagementBackend.dto.LoginRequest;
import com.example.GymManagementBackend.dto.AuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling all user registration and authentication operations.
 * This includes public endpoints for member registration and login, as well as
 * admin-protected endpoints for registering staff and other admins.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;


    /**
     * Public endpoint for new users to register as a MEMBER.
     *
     * @param request The user registration details.
     * @return A ResponseEntity with the newly created User.
     */
    @PostMapping("/register/member")
    public ResponseEntity<User> registerMember(@RequestBody UserRegistrationRequest request) {
        User newUser = authService.registerMember(request);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    /**
     * Admin-only endpoint to register a new user with the ADMIN role.
     *
     * @param request The user registration details.
     * @return A ResponseEntity with the newly created User.
     */
    @PostMapping("/register/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> registerAdmin(@RequestBody UserRegistrationRequest request) {
        User newAdmin = authService.registerAdmin(request);
        return new ResponseEntity<>(newAdmin, HttpStatus.CREATED);
    }

    /**
     * Admin-only endpoint to register a new user with the STAFF role.
     *
     * @param request The user registration details.
     * @return A ResponseEntity with the newly created User.
     */
    @PostMapping("/register/staff")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> registerStaff(@RequestBody UserRegistrationRequest request) {
        User newStaff = authService.registerStaff(request);
        return new ResponseEntity<>(newStaff, HttpStatus.CREATED);
    }

    /**
     * Public endpoint for users to log in and receive a JWT.
     *
     * @param request The user's login credentials (email and password).
     * @return A ResponseEntity containing the authentication token.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@RequestBody LoginRequest request) {
        // Step 1: Authenticate the user with Spring Security's AuthenticationManager.
        // This will throw an exception if the credentials are bad.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        // Step 2: If authentication is successful, fetch the user details.
        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.email());

        // Step 3: Generate a JWT for the authenticated user.
        final String token = jwtService.generateToken(userDetails);

        // Step 4: Return the token in the response.
        return ResponseEntity.ok(new AuthResponse(token));
    }
}