package com.librarymanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

/**
 * =====================================================
 * Book Entity - Maps to "books" table in MySQL
 * =====================================================
 * Represents a book in the library.
 *
 * Key fields:
 *   - quantity         = total copies owned by library
 *   - availableQuantity = copies currently available
 *     (decreases when issued, increases when returned)
 * =====================================================
 */
@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Book title (e.g., "Clean Code")
    @NotBlank(message = "Title is required")
    @Column(nullable = false)
    private String title;

    // Author name (e.g., "Robert C. Martin")
    @NotBlank(message = "Author is required")
    @Column(nullable = false)
    private String author;

    // Category (e.g., "Science", "Fiction", "Technology")
    @Column
    private String category;

    // Total copies in the library
    @Min(value = 1, message = "Quantity must be at least 1")
    @Column(nullable = false)
    private int quantity;

    // Available copies (not issued yet)
    // This is updated automatically when issuing/returning
    @Column(nullable = false)
    private int availableQuantity;

    /**
     * One Book can be issued to Many users over time.
     * mappedBy = "book" means IssuedBook manages the relationship.
     */
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<IssuedBook> issuedBooks;
}
