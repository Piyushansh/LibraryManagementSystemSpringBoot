package com.librarymanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * =====================================================
 * LibraryManagementApplication - ENTRY POINT
 * =====================================================
 * This is the starting point of our Spring Boot app.
 *
 * @SpringBootApplication combines:
 *   - @Configuration      → marks this as a config class
 *   - @EnableAutoConfiguration → auto-configures Spring
 *   - @ComponentScan      → scans all components in package
 *
 * When you run this class, the app starts on port 8080.
 * Open browser: http://localhost:8080
 * =====================================================
 */
@SpringBootApplication
public class LibraryManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryManagementApplication.class, args);
        System.out.println("========================================");
        System.out.println("  Library Management System Started!   ");
        System.out.println("  URL: http://localhost:8080            ");
        System.out.println("========================================");
    }
}
