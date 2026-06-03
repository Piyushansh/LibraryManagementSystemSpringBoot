package com.librarymanagement.exception;

/**
 * =====================================================
 * ResourceNotFoundException - Custom Exception
 * =====================================================
 * Used when we try to find something (book, user) that
 * doesn't exist in the database.
 *
 * Example: findById(999) → book 999 not found →
 *          throw new ResourceNotFoundException("Book not found")
 *
 * RuntimeException = unchecked exception (no need to declare
 * with "throws" in method signatures).
 * =====================================================
 */
public class ResourceNotFoundException extends RuntimeException {

    // Constructor that accepts an error message
    public ResourceNotFoundException(String message) {
        super(message); // pass message to parent RuntimeException
    }
}
