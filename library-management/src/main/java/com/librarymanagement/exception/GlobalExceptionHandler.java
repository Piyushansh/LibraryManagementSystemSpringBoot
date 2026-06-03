package com.librarymanagement.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * =====================================================
 * GlobalExceptionHandler - Catches All Errors
 * =====================================================
 * @ControllerAdvice means: "watch ALL controllers
 * and handle any exceptions thrown from them."
 *
 * Without this, if an error occurs, Spring shows an
 * ugly white error page. This class shows our own
 * clean error page instead.
 * =====================================================
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles ResourceNotFoundException (e.g., book not found).
     * Returns our custom error page with the error message.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public String handleNotFound(ResourceNotFoundException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("errorType", "Resource Not Found");
        return "error"; // returns error.html template
    }

    /**
     * Handles any other unexpected exceptions.
     * This is a safety net for all other errors.
     */
    @ExceptionHandler(Exception.class)
    public String handleGenericError(Exception ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("errorType", "Something went wrong");
        return "error";
    }
}
