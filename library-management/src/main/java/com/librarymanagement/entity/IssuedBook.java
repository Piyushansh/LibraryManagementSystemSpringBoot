package com.librarymanagement.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * =====================================================
 * IssuedBook Entity - Maps to "issued_books" table
 * =====================================================
 * Tracks which user has been issued which book.
 *
 * Relationship:
 *   - Many IssuedBooks can belong to ONE User
 *   - Many IssuedBooks can belong to ONE Book
 *   (Many-to-One on both sides)
 *
 * Fine Calculation Logic:
 *   - Due date = issue date + 14 days
 *   - If return_date > due_date → fine applies
 *   - Fine = ₹2 per extra day
 * =====================================================
 */
@Entity
@Table(name = "issued_books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IssuedBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Many IssuedBooks → One User
     * @JoinColumn creates a foreign key column "user_id" in issued_books table
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Many IssuedBooks → One Book
     * @JoinColumn creates a foreign key column "book_id" in issued_books table
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    // Date when the book was issued
    @Column(nullable = false)
    private LocalDate issueDate;

    // Due date = issueDate + 14 days (set automatically)
    @Column(nullable = false)
    private LocalDate dueDate;

    // Date when the book was returned (null if not yet returned)
    @Column
    private LocalDate returnDate;

    // Fine amount in Rupees (0 if returned on time)
    @Column(nullable = false)
    private double fine;

    // Status: "ISSUED" or "RETURNED"
    @Column(nullable = false)
    private String status;
}
