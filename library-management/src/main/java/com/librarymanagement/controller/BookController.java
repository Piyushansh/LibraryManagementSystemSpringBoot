package com.librarymanagement.controller;

import com.librarymanagement.entity.Book;
import com.librarymanagement.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * =====================================================
 * BookController - Book CRUD Operations
 * =====================================================
 * Handles:
 *   GET  /books           → list all books (with search + pagination)
 *   GET  /books/add       → show add book form (ADMIN only)
 *   POST /books/add       → save new book    (ADMIN only)
 *   GET  /books/edit/{id} → show edit form   (ADMIN only)
 *   POST /books/edit/{id} → save changes     (ADMIN only)
 *   GET  /books/delete/{id} → delete book    (ADMIN only)
 *
 * @PreAuthorize("hasRole('ADMIN')") → restricts to ADMIN
 * This is METHOD-level security (extra protection layer).
 * =====================================================
 */
@Controller
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    // --- List All Books (with Search + Pagination) ---
    @GetMapping
    public String listBooks(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            Model model) {

        // Fetch paginated results (search or all)
        Page<Book> bookPage = bookService.searchBooks(keyword, page, size);

        model.addAttribute("books",       bookPage.getContent());  // list of books for this page
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages",  bookPage.getTotalPages());
        model.addAttribute("totalItems",  bookPage.getTotalElements());
        model.addAttribute("keyword",     keyword);
        model.addAttribute("size",        size);

        return "books"; // → templates/books.html
    }

    // --- Show Add Book Form (Admin only) ---
    @GetMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public String showAddForm(Model model) {
        model.addAttribute("book", new Book()); // empty book object for the form
        return "book-form"; // → templates/book-form.html
    }

    // --- Save New Book (Admin only) ---
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public String addBook(@Valid @ModelAttribute("book") Book book,
                           BindingResult result,
                           RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "book-form";
        }
        bookService.addBook(book);
        redirectAttributes.addFlashAttribute("successMessage",
            "Book '" + book.getTitle() + "' added successfully!");
        return "redirect:/books";
    }

    // --- Show Edit Book Form (Admin only) ---
    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String showEditForm(@PathVariable Long id, Model model) {
        Book book = bookService.getBookById(id);
        model.addAttribute("book", book);
        model.addAttribute("editMode", true); // flag to change form title
        return "book-form"; // reuse same form for add and edit
    }

    // --- Save Edited Book (Admin only) ---
    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateBook(@PathVariable Long id,
                              @Valid @ModelAttribute("book") Book book,
                              BindingResult result,
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "book-form";
        }
        bookService.updateBook(id, book);
        redirectAttributes.addFlashAttribute("successMessage",
            "Book updated successfully!");
        return "redirect:/books";
    }

    // --- Delete Book (Admin only) ---
    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteBook(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Book book = bookService.getBookById(id);
        bookService.deleteBook(id);
        redirectAttributes.addFlashAttribute("successMessage",
            "Book '" + book.getTitle() + "' deleted successfully!");
        return "redirect:/books";
    }

    // --- View Book Details ---
    @GetMapping("/view/{id}")
    public String viewBook(@PathVariable Long id, Model model) {
        model.addAttribute("book", bookService.getBookById(id));
        return "book-detail";
    }
}
