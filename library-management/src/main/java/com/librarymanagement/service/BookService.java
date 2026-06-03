package com.librarymanagement.service;

import com.librarymanagement.entity.Book;
import com.librarymanagement.exception.ResourceNotFoundException;
import com.librarymanagement.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * =====================================================
 * BookService - Business Logic for Books
 * =====================================================
 * Handles all book-related operations:
 *   - Add a new book
 *   - Update book details
 *   - Delete a book
 *   - Search books
 *   - Get all books (with pagination)
 * =====================================================
 */
@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    /**
     * Add a new book to the library.
     * Sets availableQuantity = quantity when first added.
     */
    public Book addBook(Book book) {
        // Initially, all copies are available
        book.setAvailableQuantity(book.getQuantity());
        return bookRepository.save(book);
    }

    /**
     * Update an existing book's details.
     * Only updates title, author, category, and quantity.
     * Available quantity is adjusted based on the change.
     */
    public Book updateBook(Long id, Book updatedBook) {
        // First, find the existing book (throws exception if not found)
        Book existingBook = getBookById(id);

        // Calculate how many books are currently issued
        int currentlyIssued = existingBook.getQuantity() - existingBook.getAvailableQuantity();

        // Update fields
        existingBook.setTitle(updatedBook.getTitle());
        existingBook.setAuthor(updatedBook.getAuthor());
        existingBook.setCategory(updatedBook.getCategory());
        existingBook.setQuantity(updatedBook.getQuantity());

        // Recalculate available = new total - currently issued
        int newAvailable = updatedBook.getQuantity() - currentlyIssued;
        existingBook.setAvailableQuantity(Math.max(newAvailable, 0)); // don't go below 0

        return bookRepository.save(existingBook);
    }

    /**
     * Delete a book from the library.
     */
    public void deleteBook(Long id) {
        // Verify book exists before deleting
        Book book = getBookById(id);
        bookRepository.delete(book);
    }

    /**
     * Get a single book by ID.
     * Throws ResourceNotFoundException if not found.
     */
    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
    }

    /**
     * Get all books with pagination and sorting.
     * page = 0-indexed page number
     * size = number of books per page
     */
    public Page<Book> getAllBooks(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("title").ascending());
        return bookRepository.findAll(pageable);
    }

    /**
     * Search books by title or author with pagination.
     * If keyword is empty, return all books.
     */
    public Page<Book> searchBooks(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("title").ascending());
        if (keyword == null || keyword.trim().isEmpty()) {
            return bookRepository.findAll(pageable);
        }
        return bookRepository.searchBooks(keyword.trim(), pageable);
    }

    /**
     * Get all available books (for issuing).
     */
    public List<Book> getAvailableBooks() {
        return bookRepository.findByAvailableQuantityGreaterThan(0);
    }

    /**
     * Count total books in the library (for dashboard).
     */
    public long getTotalBooks() {
        return bookRepository.count();
    }

    /**
     * Get a simple list of all books (for dropdowns).
     */
    public List<Book> getAllBooksList() {
        return bookRepository.findAll(Sort.by("title").ascending());
    }
}
