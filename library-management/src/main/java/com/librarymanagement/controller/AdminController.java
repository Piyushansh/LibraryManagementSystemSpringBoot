package com.librarymanagement.controller;

import com.librarymanagement.entity.User;
import com.librarymanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * =====================================================
 * AdminController - Admin-Only Operations
 * =====================================================
 * @PreAuthorize("hasRole('ADMIN')") on the class means
 * ALL methods in this controller require ADMIN role.
 *
 * Endpoints:
 *   GET /admin/users  → view all users
 *   GET /admin/users/{id} → view user profile
 * =====================================================
 */
@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private UserService userService;

    // --- View All Users ---
    @GetMapping("/users")
    public String viewAllUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin-users"; // → templates/admin-users.html
    }

    // --- View Single User Profile ---
    @GetMapping("/users/{id}")
    public String viewUserProfile(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "user-profile"; // → templates/user-profile.html
    }
}
