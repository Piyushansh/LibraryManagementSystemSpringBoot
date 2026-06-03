package com.librarymanagement.service;

import com.librarymanagement.dto.UserDTO;
import com.librarymanagement.entity.User;
import com.librarymanagement.exception.ResourceNotFoundException;
import com.librarymanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * =====================================================
 * UserService - Business Logic for Users
 * =====================================================
 * Service layer = where business rules live.
 * Controller calls Service, Service calls Repository.
 *
 * Flow:
 *   Browser → Controller → Service → Repository → Database
 *                        ↑ (we are here)
 *
 * @Service → Spring registers this as a service bean.
 * @Autowired → Spring automatically injects dependencies.
 * =====================================================
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // BCrypt encoder from SecurityConfig

    /**
     * Register a new user.
     * - Checks if email is already taken
     * - Hashes the password before saving (NEVER store plain text!)
     * - Sets default role to ROLE_USER if not provided
     */
    public User registerUser(UserDTO userDTO) {

        // Check if email already exists in database
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new RuntimeException("Email already registered: " + userDTO.getEmail());
        }

        // Create a new User entity from the DTO
        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());

        // IMPORTANT: Encode the password using BCrypt before saving
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        // Set role - default to ROLE_USER if not specified
        String role = (userDTO.getRole() != null && !userDTO.getRole().isEmpty())
                ? userDTO.getRole()
                : "ROLE_USER";
        user.setRole(role);

        // Save to database and return the saved entity
        return userRepository.save(user);
    }

    /**
     * Get all users (for admin dashboard).
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Find a specific user by their ID.
     * Throws exception if user not found.
     */
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    /**
     * Find a user by their email.
     */
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    /**
     * Count total number of users (for dashboard).
     */
    public long getTotalUsers() {
        return userRepository.count();
    }
}
