package com.librarymanagement.repository;

import com.librarymanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * =====================================================
 * UserRepository - Database access for Users
 * =====================================================
 * Repository = the layer that talks to the database.
 *
 * By extending JpaRepository<User, Long>, we get
 * FREE built-in methods:
 *   - save(user)         → insert or update
 *   - findById(id)       → find by primary key
 *   - findAll()          → get all records
 *   - deleteById(id)     → delete a record
 *   - count()            → count all records
 *
 * We only need to add CUSTOM queries here.
 * Spring automatically generates the SQL!
 *
 * Method name magic (Spring Data JPA):
 *   findByEmail → SELECT * FROM users WHERE email = ?
 *   existsByEmail → SELECT COUNT(*) FROM users WHERE email = ?
 * =====================================================
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find a user by their email (used in login)
    Optional<User> findByEmail(String email);

    // Check if a user already exists with this email
    boolean existsByEmail(String email);
}
