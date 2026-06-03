package com.librarymanagement.repository;

import com.librarymanagement.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * =====================================================
 * BookRepository - Database access for Books
 * =====================================================
 * Custom methods:
 *
 * searchBooks() uses @Query with JPQL (Java Persistence
 * Query Language). JPQL is like SQL but uses entity names
 * instead of table names, and field names instead of columns.
 *
 * "b" is an alias for Book entity.
 * LOWER() converts to lowercase so search is case-insensitive.
 * LIKE '%...%' means "contains this text anywhere".
 * =====================================================
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * Search books by title OR author (case-insensitive).
     * Returns a Page (for pagination support).
     *
     * @param keyword  - search term
     * @param pageable - pagination info (page number, size)
     */
    @Query("SELECT b FROM Book b WHERE " +
           "LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(b.author) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Book> searchBooks(@Param("keyword") String keyword, Pageable pageable);

    /**
     * Simple search returning a list (used in dropdowns).
     */
    @Query("SELECT b FROM Book b WHERE " +
           "LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(b.author) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Book> searchBooksList(@Param("keyword") String keyword);

    // Find books by category
    List<Book> findByCategory(String category);

    // Find books that have at least 1 available copy
    List<Book> findByAvailableQuantityGreaterThan(int quantity);
}
