package com.librarymanagement.controller;

import com.librarymanagement.dto.UserDTO;
import com.librarymanagement.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * =====================================================
 * AuthController - Handles Login & Registration
 * =====================================================
 * @Controller → returns HTML views (not JSON)
 * @RequestMapping → base URL for this controller
 *
 * Endpoints:
 *   GET  /login    → show login page
 *   GET  /register → show register page
 *   POST /register → process registration form
 * =====================================================
 */
@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    // --- Show Login Page ---
    @GetMapping("/login")
    public String showLoginPage(@RequestParam(value = "error", required = false) String error,
                                 @RequestParam(value = "logout", required = false) String logout,
                                 Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Invalid email or password. Please try again.");
        }
        if (logout != null) {
            model.addAttribute("successMessage", "You have been logged out successfully.");
        }
        return "login"; // → templates/login.html
    }

    // --- Show Registration Page ---
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("userDTO", new UserDTO()); // empty form object
        return "register"; // → templates/register.html
    }

    // --- Process Registration Form ---
    @PostMapping("/register")
    public String processRegistration(@Valid @ModelAttribute("userDTO") UserDTO userDTO,
                                       BindingResult result,
                                       RedirectAttributes redirectAttributes,
                                       Model model) {
        // If validation errors exist, show form again with errors
        if (result.hasErrors()) {
            return "register";
        }

        try {
            userService.registerUser(userDTO);
            redirectAttributes.addFlashAttribute("successMessage",
                "Registration successful! Please login.");
            return "redirect:/login";

        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        }
    }
}
