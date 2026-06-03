package com.librarymanagement.config;

import com.librarymanagement.entity.User;
import com.librarymanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * =====================================================
 * CustomUserDetailsService - Spring Security Hook
 * =====================================================
 * Spring Security needs to know HOW to load a user
 * from OUR database when someone tries to log in.
 *
 * We implement UserDetailsService and override
 * loadUserByUsername() — Spring calls this automatically
 * during login to verify credentials.
 *
 * Flow:
 *   1. User enters email + password on login form
 *   2. Spring calls loadUserByUsername(email)
 *   3. We fetch user from database by email
 *   4. We return a UserDetails object with password + role
 *   5. Spring compares the entered password with stored hash
 *   6. If match → login success, else → login failed
 * =====================================================
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Called by Spring Security during login.
     * "username" in our app = email address.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // Find user by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No user found with email: " + email));

        // Convert our User entity to Spring's UserDetails
        // SimpleGrantedAuthority wraps the role string ("ROLE_ADMIN", "ROLE_USER")
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole()))
        );
    }
}
