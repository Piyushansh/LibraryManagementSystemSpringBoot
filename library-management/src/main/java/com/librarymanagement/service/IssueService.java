package com.librarymanagement.service;

import com.librarymanagement.entity.Book;
import com.librarymanagement.entity.IssuedBook;
import com.librarymanagement.entity.User;
import com.librarymanagement.exception.ResourceNotFoundException;
import com.librarymanagement.repository.BookRepository;
import com.librarymanagement.repository.IssuedBookRepository;
import com.librarymanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * =====================================================
 * IssueService - Business Logic for Issue/Return
 * =====================================================
 * Handles:
 *   - Issuing a book to a user
 *   - Returning a book
 *   - Fine calculation
 *
 * @Transactional = if something fails mid-operation,
 * ALL changes are rolled back (like a database transaction).
 * This prevents corrupt data (e.g., book quantity updated
 * but issued record not created).
 * =====================================================
 */
@Service
public class IssueService {

    // Fine amount per extra day (in Rupees)
    private static final double FINE_PER_DAY = 2.0;

    // Number of days allowed before due date
    private static final int LOAN_PERIOD_DAYS = 14;

    @Autowired
    private IssuedBookRepository issuedBookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    /**
     * Issue a book to a user.
     *
     * Steps:
     *   1. Find the user and book
     *   2. Check if book is available
     *   3. Check if user already has this book
     *   4. Create IssuedBook record
     *   5. Decrease availableQuantity by 1
     *
     * @Transactional ensures both saves happen together.
     */
    @Transactional
    public IssuedBook issueBook(Long userId, Long bookId) {

        // Step 1: Find user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        // Step 2: Find book
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found: " + bookId));

        // Step 3: Check if book is available
        if (book.getAvailableQuantity() <= 0) {
            throw new RuntimeException("Book is not available: " + book.getTitle());
        }

        // Step 4: Check if user already has this book issued (and not returned)
        boolean alreadyIssued = issuedBookRepository
                .findByUserAndBookAndStatus(user, book, "ISSUED")
                .isPresent();

        if (alreadyIssued) {
            throw new RuntimeException("User already has this book issued!");
        }

        // Step 5: Create the issued book record
        IssuedBook issuedBook = new IssuedBook();
        issuedBook.setUser(user);
        issuedBook.setBook(book);
        issuedBook.setIssueDate(LocalDate.now());
        issuedBook.setDueDate(LocalDate.now().plusDays(LOAN_PERIOD_DAYS)); // Due in 14 days
        issuedBook.setReturnDate(null);   // Not returned yet
        issuedBook.setFine(0.0);          // No fine yet
        issuedBook.setStatus("ISSUED");

        // Step 6: Decrease available quantity
        book.setAvailableQuantity(book.getAvailableQuantity() - 1);
        bookRepository.save(book); // save updated book

        return issuedBookRepository.save(issuedBook); // save issued record
    }

    /**
     * Return a book.
     *
     * Steps:
     *   1. Find the active issue record
     *   2. Calculate fine if overdue
     *   3. Mark as RETURNED
     *   4. Increase availableQuantity by 1
     *
     * Fine Logic:
     *   - If returned after due date → fine = extra days × ₹2
     *   - If returned on time → fine = ₹0
     */
    @Transactional
    public IssuedBook returnBook(Long issuedBookId) {

        // Find the issued book record
        IssuedBook issuedBook = issuedBookRepository.findById(issuedBookId)
                .orElseThrow(() -> new ResourceNotFoundException("Issue record not found: " + issuedBookId));

        // Can't return a book that's already returned
        if ("RETURNED".equals(issuedBook.getStatus())) {
            throw new RuntimeException("Book already returned!");
        }

        // Set return date to today
        LocalDate today = LocalDate.now();
        issuedBook.setReturnDate(today);

        // Calculate fine if returned late
        double fine = calculateFine(issuedBook.getDueDate(), today);
        issuedBook.setFine(fine);
        issuedBook.setStatus("RETURNED");

        // Increase available quantity (book is back in library)
        Book book = issuedBook.getBook();
        book.setAvailableQuantity(book.getAvailableQuantity() + 1);
        bookRepository.save(book);

        return issuedBookRepository.save(issuedBook);
    }

    /**
     * Calculate fine for late return.
     *
     * @param dueDate    - when the book was supposed to be returned
     * @param returnDate - when the book was actually returned
     * @return fine amount in Rupees
     */
    private double calculateFine(LocalDate dueDate, LocalDate returnDate) {
        if (returnDate.isAfter(dueDate)) {
            // ChronoUnit.DAYS.between() gives number of days between two dates
            long overdueDays = ChronoUnit.DAYS.between(dueDate, returnDate);
            return overdueDays * FINE_PER_DAY;
        }
        return 0.0; // No fine if returned on time
    }

    /**
     * Get all currently issued books (for admin).
     */
    public List<IssuedBook> getAllIssuedBooks() {
        return issuedBookRepository.findByStatus("ISSUED");
    }

    /**
     * Get all issued books (both issued and returned).
     */
    public List<IssuedBook> getAllIssuedBooksHistory() {
        return issuedBookRepository.findAll();
    }

    /**
     * Get issued books for a specific user.
     */
    public List<IssuedBook> getBooksByUser(User user) {
        return issuedBookRepository.findByUser(user);
    }

    /**
     * Count of currently issued books (for dashboard).
     */
    public long getCurrentlyIssuedCount() {
        return issuedBookRepository.countByStatus("ISSUED");
    }
}
