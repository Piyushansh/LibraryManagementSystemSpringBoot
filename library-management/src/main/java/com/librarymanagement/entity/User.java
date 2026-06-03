package com.librarymanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

/**
 * =====================================================
 * User Entity - Maps to "users" table in MySQL
 * =====================================================
 * @Entity   → tells JPA this class is a database table
 * @Table    → sets the actual table name in MySQL
 *
 * Each field with @Column becomes a column in the table.
 * Lombok annotations remove the need to write
 * getters, setters, constructors manually.
 * =====================================================
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    // PRIMARY KEY - auto generated (1, 2, 3, ...)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // User's full name - cannot be empty
    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String name;

    // Email must be unique and valid format
    @Email(message = "Please provide a valid email")
    @NotBlank(message = "Email is required")
    @Column(nullable = false, unique = true)
    private String email;

    // Password (will be stored as BCrypt hash, NOT plain text)
    @NotBlank(message = "Password is required")
    @Column(nullable = false)
    private String password;

    // Role: either "ROLE_ADMIN" or "ROLE_USER"
    // Spring Security requires "ROLE_" prefix
    @Column(nullable = false)
    private String role;

    /**
     * One User can have Many issued books.
     * mappedBy = "user" means the relationship is managed
     * from the IssuedBook side (the "user" field in IssuedBook).
     * CascadeType.ALL = if user is deleted, their issued records also delete.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<IssuedBook> issuedBooks;
}
