package com.librarymanagement.controller;

import com.librarymanagement.entity.IssuedBook;
import com.librarymanagement.entity.User;
import com.librarymanagement.service.BookService;
import com.librarymanagement.service.IssueService;
import com.librarymanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * =====================================================
 * IssueController - Book Issue & Return
 * =====================================================
 * Endpoints:
 *   GET  /issues              → admin: all issued books
 *   GET  /issues/my           → user: my issued books
 *   GET  /issues/new          → admin: issue book form
 *   POST /issues/new          → admin: submit issue
 *   GET  /issues/return/{id}  → admin: return a book
 * =====================================================
 */
@Controller
@RequestMapping("/issues")
public class IssueController {

    @Autowired
    private IssueService issueService;

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    // --- Admin: View All Issued Books ---
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String viewAllIssues(Model model) {
        List<IssuedBook> allIssues = issueService.getAllIssuedBooksHistory();
        model.addAttribute("issuedBooks",  allIssues);
        model.addAttribute("currentlyOut", issueService.getCurrentlyIssuedCount());
        return "issues"; // → templates/issues.html
    }

    // --- User: View My Issued Books ---
    @GetMapping("/my")
    public String viewMyIssues(Authentication authentication, Model model) {
        // Get the currently logged-in user
        User user = userService.getUserByEmail(authentication.getName());
        List<IssuedBook> myBooks = issueService.getBooksByUser(user);
        model.addAttribute("issuedBooks", myBooks);
        model.addAttribute("pageTitle", "My Issued Books");
        return "issues"; // reuse same template
    }

    // --- Admin: Show Issue Book Form ---
    @GetMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String showIssueForm(Model model) {
        model.addAttribute("users", userService.getAllUsers());         // all users for dropdown
        model.addAttribute("books", bookService.getAvailableBooks());  // only available books
        return "issue-form"; // → templates/issue-form.html
    }

    // --- Admin: Process Issue Book Submission ---
    @PostMapping("/new")
    @PreAuthorize("hasRole('ADMIN')")
    public String issueBook(@RequestParam Long userId,
                             @RequestParam Long bookId,
                             RedirectAttributes redirectAttributes) {
        try {
            IssuedBook issued = issueService.issueBook(userId, bookId);
            redirectAttributes.addFlashAttribute("successMessage",
                "Book '" + issued.getBook().getTitle() + "' issued to " +
                issued.getUser().getName() + ". Due: " + issued.getDueDate());
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/issues";
    }

    // --- Admin: Return a Book ---
    @GetMapping("/return/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String returnBook(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            IssuedBook returned = issueService.returnBook(id);
            String msg = "Book '" + returned.getBook().getTitle() + "' returned.";
            if (returned.getFine() > 0) {
                msg += " Fine collected: ₹" + returned.getFine();
            }
            redirectAttributes.addFlashAttribute("successMessage", msg);
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/issues";
    }
}
