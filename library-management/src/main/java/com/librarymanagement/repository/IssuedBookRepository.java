package com.librarymanagement.repository;

import com.librarymanagement.entity.IssuedBook;
import com.librarymanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * =====================================================
 * IssuedBookRepository - Database access for Issued Books
 * =====================================================
 * Handles queries related to book issuing and returning.
 *
 * Spring Data JPA auto-generates SQL from method names:
 *   findByUser       → WHERE user_id = ?
 *   findByStatus     → WHERE status = ?
 *   findByUserAndBook → WHERE user_id = ? AND book_id = ?
 * =====================================================
 */
@Repository
public interface IssuedBookRepository extends JpaRepository<IssuedBook, Long> {

    // Get all books issued to a specific user
    List<IssuedBook> findByUser(User user);

    // Get all records with a specific status ("ISSUED" or "RETURNED")
    List<IssuedBook> findByStatus(String status);

    // Find active (not yet returned) issue record for a specific user and book
    Optional<IssuedBook> findByUserAndBookAndStatus(
        com.librarymanagement.entity.User user,
        com.librarymanagement.entity.Book book,
        String status
    );

    // Count how many books are currently issued (not returned)
    long countByStatus(String status);

    // Get issued books for a specific user that are not yet returned
    List<IssuedBook> findByUserAndStatus(User user, String status);
}
