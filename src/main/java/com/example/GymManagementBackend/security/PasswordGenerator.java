package com.example.GymManagementBackend.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "j123";
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println("BCrypt Hash for '" + rawPassword + "':");
        System.out.println(encodedPassword);
    }
}