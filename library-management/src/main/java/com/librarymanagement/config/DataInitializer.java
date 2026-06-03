package com.librarymanagement.config;

import com.librarymanagement.entity.Book;
import com.librarymanagement.entity.User;
import com.librarymanagement.repository.BookRepository;
import com.librarymanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * =====================================================
 * DataInitializer - Seeds Default Data on First Run
 * =====================================================
 * CommandLineRunner runs ONCE when the app starts.
 * It checks if the database is empty; if so, it inserts
 * a default admin account and some sample books.
 *
 * This means you can run the app WITHOUT running
 * the SQL script manually. The tables are created by
 * Hibernate (spring.jpa.hibernate.ddl-auto=update)
 * and data is seeded here.
 *
 * Default credentials created automatically:
 *   Admin: admin@library.com / admin123
 * =====================================================
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        // Only seed if database is empty (first run)
        if (userRepository.count() == 0) {
            System.out.println("=== Seeding initial data ===");

            // Create default Admin
            User admin = new User();
            admin.setName("Admin User");
            admin.setEmail("admin@library.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ROLE_ADMIN");
            userRepository.save(admin);

            // Create default regular user
            User user = new User();
            user.setName("Demo User");
            user.setEmail("user@library.com");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setRole("ROLE_USER");
            userRepository.save(user);

            System.out.println("✓ Default users created");
            System.out.println("  Admin → admin@library.com / admin123");
            System.out.println("  User  → user@library.com / user123");
        }

        if (bookRepository.count() == 0) {
            // Seed sample books
            String[][] books = {
                {"Clean Code",                  "Robert C. Martin", "Technology",  "5"},
                {"The Pragmatic Programmer",    "Andrew Hunt",      "Technology",  "3"},
                {"Introduction to Algorithms",  "Cormen et al.",    "Mathematics", "4"},
                {"Head First Java",             "Kathy Sierra",     "Technology",  "6"},
                {"Operating System Concepts",   "Silberschatz",     "Engineering", "5"},
                {"Database System Concepts",    "Silberschatz",     "Engineering", "4"},
                {"Computer Networks",           "Andrew Tanenbaum", "Engineering", "4"},
                {"A Brief History of Time",     "Stephen Hawking",  "Science",     "5"},
                {"Atomic Habits",               "James Clear",      "Business",    "6"},
                {"The Alchemist",               "Paulo Coelho",     "Fiction",     "4"},
                {"Sapiens",                     "Yuval Noah Harari","History",     "3"},
                {"Spring Boot in Action",       "Craig Walls",      "Technology",  "3"},
            };

            for (String[] b : books) {
                Book book = new Book();
                book.setTitle(b[0]);
                book.setAuthor(b[1]);
                book.setCategory(b[2]);
                book.setQuantity(Integer.parseInt(b[3]));
                book.setAvailableQuantity(Integer.parseInt(b[3])); // all available initially
                bookRepository.save(book);
            }

            System.out.println("✓ " + books.length + " sample books added");
        }
    }
}
