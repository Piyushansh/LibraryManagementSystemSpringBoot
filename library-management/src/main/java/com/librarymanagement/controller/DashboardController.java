package com.librarymanagement.controller;

import com.librarymanagement.service.BookService;
import com.librarymanagement.service.IssueService;
import com.librarymanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * =====================================================
 * DashboardController - Main Dashboard
 * =====================================================
 * Shows statistics to both admins and users.
 *
 * Authentication object → provided by Spring Security.
 * It contains info about the currently logged-in user
 * (email, roles, etc.).
 * =====================================================
 */
@Controller
public class DashboardController {

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    @Autowired
    private IssueService issueService;

    // --- Show Dashboard ---
    @GetMapping("/dashboard")
    public String showDashboard(Model model, Authentication authentication) {

        // Add statistics to the model (accessible in HTML via Thymeleaf)
        model.addAttribute("totalBooks",   bookService.getTotalBooks());
        model.addAttribute("totalUsers",   userService.getTotalUsers());
        model.addAttribute("issuedBooks",  issueService.getCurrentlyIssuedCount());

        // Recent issued books (last 5)
        model.addAttribute("recentIssues", issueService.getAllIssuedBooksHistory()
                .stream().limit(5).toList());

        // Logged-in user's email
        model.addAttribute("currentUser", authentication.getName());

        // Check if admin
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("isAdmin", isAdmin);

        return "dashboard"; // → templates/dashboard.html
    }

    // Redirect root URL to dashboard
    @GetMapping("/")
    public String redirectToDashboard() {
        return "redirect:/dashboard";
    }
}
