-- =====================================================
-- Library Management System - MySQL Setup Script
-- =====================================================
-- Run this script in MySQL Workbench before starting
-- the Spring Boot application.
--
-- Steps:
--   1. Open MySQL Workbench
--   2. Connect to your local MySQL server
--   3. Open this file (File > Open SQL Script)
--   4. Click the ⚡ Execute button
-- =====================================================


-- Step 1: Create and use the database
CREATE DATABASE IF NOT EXISTS library_db;
USE library_db;

-- =====================================================
-- TABLE: users
-- =====================================================
CREATE TABLE IF NOT EXISTS users (
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    name     VARCHAR(100) NOT NULL,
    email    VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,    -- BCrypt hashed
    role     VARCHAR(20)  NOT NULL,    -- ROLE_ADMIN or ROLE_USER
    PRIMARY KEY (id)
);

-- =====================================================
-- TABLE: books
-- =====================================================
CREATE TABLE IF NOT EXISTS books (
    id                 BIGINT       NOT NULL AUTO_INCREMENT,
    title              VARCHAR(200) NOT NULL,
    author             VARCHAR(150) NOT NULL,
    category           VARCHAR(100),
    quantity           INT          NOT NULL DEFAULT 1,
    available_quantity INT          NOT NULL DEFAULT 1,
    PRIMARY KEY (id)
);

-- =====================================================
-- TABLE: issued_books
-- =====================================================
CREATE TABLE IF NOT EXISTS issued_books (
    id          BIGINT NOT NULL AUTO_INCREMENT,
    user_id     BIGINT NOT NULL,
    book_id     BIGINT NOT NULL,
    issue_date  DATE   NOT NULL,
    due_date    DATE   NOT NULL,
    return_date DATE,
    fine        DOUBLE NOT NULL DEFAULT 0.0,
    status      VARCHAR(20) NOT NULL DEFAULT 'ISSUED',   -- ISSUED or RETURNED
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (book_id) REFERENCES books(id)
);

-- =====================================================
-- SAMPLE DATA
-- =====================================================
-- NOTE: These users' passwords are BCrypt hashes.
-- admin@library.com → password: admin123
-- user@library.com  → password: user123
-- (BCrypt hash generated with strength 10)

INSERT INTO users (name, email, password, role) VALUES
('Admin User',  'admin@library.com',
 '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBpwTTyRTa3W2W',
 'ROLE_ADMIN'),
('Aayush Sharma', 'user@library.com',
 '$2a$10$8K1p/a0dL1LXMIgoEDFrwOJFsVT1HLVGsUuOPm7DuiSZ1SLKnSOZK',
 'ROLE_USER');

-- Sample Books
INSERT INTO books (title, author, category, quantity, available_quantity) VALUES
('Clean Code',                           'Robert C. Martin',   'Technology',    5, 5),
('The Pragmatic Programmer',             'Andrew Hunt',        'Technology',    3, 3),
('Introduction to Algorithms',           'Cormen et al.',      'Mathematics',   4, 4),
('Design Patterns',                      'Gang of Four',       'Technology',    3, 3),
('Head First Java',                      'Kathy Sierra',       'Technology',    6, 6),
('Operating System Concepts',            'Silberschatz',       'Engineering',   5, 5),
('Database System Concepts',             'Silberschatz',       'Engineering',   4, 4),
('Computer Networks',                    'Andrew Tanenbaum',   'Engineering',   4, 4),
('The Great Gatsby',                     'F. Scott Fitzgerald','Fiction',        3, 3),
('To Kill a Mockingbird',                'Harper Lee',         'Fiction',        2, 2),
('A Brief History of Time',              'Stephen Hawking',    'Science',        5, 5),
('The Alchemist',                        'Paulo Coelho',       'Fiction',        4, 4),
('Sapiens',                              'Yuval Noah Harari',  'History',        3, 3),
('Atomic Habits',                        'James Clear',        'Business',       6, 6),
('Spring Boot in Action',                'Craig Walls',        'Technology',     3, 3);

-- Verify the data
SELECT 'Users inserted:'  AS message, COUNT(*) AS count FROM users;
SELECT 'Books inserted:'  AS message, COUNT(*) AS count FROM books;
