# 📚 Library Management System
### Built with Spring Boot 3 · Thymeleaf · MySQL · Spring Security

---

## 🚀 QUICK START (3 Steps)

### Step 1 — Configure MySQL
Open `src/main/resources/application.properties` and update:
```properties
spring.datasource.username=root
spring.datasource.password=Piyush@1234#
```
The database `library_db` is **auto-created** on first run.

### Step 2 — Run the App
In IntelliJ IDEA → right-click `LibraryManagementApplication.java` → **Run**

### Step 3 — Open Browser
Go to: **http://localhost:8080**

**Default login credentials (auto-seeded):**
| Role  | Email                | Password  |
|-------|----------------------|-----------|
| Admin | admin@library.com    | admin123  |
| User  | user@library.com     | user123   |

---

## 🛠️ DETAILED SETUP IN INTELLIJ IDEA

### Prerequisites
- Java 17 JDK installed
- MySQL 8.x running locally
- IntelliJ IDEA (Community or Ultimate)
- Maven (bundled with IntelliJ)

### Steps
1. **Open Project**: File → Open → select `library-management` folder
2. **Wait for Maven**: IntelliJ auto-downloads all dependencies (watch bottom bar)
3. **Configure DB**: Edit `application.properties` with your MySQL credentials
4. **Run**: Click the green ▶ button next to `LibraryManagementApplication`
5. **Console output**: You should see `Library Management System Started!`
6. **Browser**: http://localhost:8080 → redirects to login

---

## 🗄️ DATABASE SETUP (Optional)
The app auto-creates tables via Hibernate (`ddl-auto=update`).  
For manual setup, run `database/schema.sql` in MySQL Workbench.

```sql
-- Quick manual setup
CREATE DATABASE IF NOT EXISTS library_db;
USE library_db;
-- Then run the full schema.sql file
```

---

## 📁 PROJECT STRUCTURE

```
library-management/
├── pom.xml                          ← Maven dependencies
├── database/
│   └── schema.sql                   ← Manual DB setup script
└── src/main/
    ├── java/com/librarymanagement/
    │   ├── LibraryManagementApplication.java   ← Entry point
    │   ├── config/
    │   │   ├── SecurityConfig.java             ← Spring Security rules
    │   │   ├── CustomUserDetailsService.java   ← Auth logic
    │   │   └── DataInitializer.java            ← Seeds default data
    │   ├── controller/
    │   │   ├── AuthController.java             ← Login, Register
    │   │   ├── DashboardController.java        ← Dashboard stats
    │   │   ├── BookController.java             ← CRUD + Search
    │   │   ├── IssueController.java            ← Issue & Return
    │   │   └── AdminController.java            ← User management
    │   ├── service/
    │   │   ├── UserService.java                ← User business logic
    │   │   ├── BookService.java                ← Book business logic
    │   │   └── IssueService.java               ← Issue/Return + Fine
    │   ├── repository/
    │   │   ├── UserRepository.java             ← DB queries for users
    │   │   ├── BookRepository.java             ← DB queries for books
    │   │   └── IssuedBookRepository.java       ← DB queries for issues
    │   ├── entity/
    │   │   ├── User.java                       ← users table
    │   │   ├── Book.java                       ← books table
    │   │   └── IssuedBook.java                 ← issued_books table
    │   ├── dto/
    │   │   ├── UserDTO.java                    ← Registration form data
    │   │   └── IssueRequest.java               ← Issue request data
    │   └── exception/
    │       ├── ResourceNotFoundException.java  ← Custom exception
    │       └── GlobalExceptionHandler.java     ← Error handler
    └── resources/
        ├── application.properties              ← App configuration
        ├── static/css/style.css                ← Custom styles
        └── templates/
            ├── fragments/layout.html           ← Navbar + Footer
            ├── login.html                      ← Login page
            ├── register.html                   ← Register page
            ├── dashboard.html                  ← Dashboard
            ├── books.html                      ← Book list + search
            ├── book-form.html                  ← Add/Edit book
            ├── book-detail.html                ← Single book view
            ├── issues.html                     ← Issue/Return list
            ├── issue-form.html                 ← Issue book form
            ├── admin-users.html                ← User list (admin)
            ├── user-profile.html               ← User detail
            └── error.html                      ← Error page
```

---

## 🔗 URL ROUTES

| URL                   | Access     | Description              |
|-----------------------|------------|--------------------------|
| `/`                   | Any        | Redirects to dashboard   |
| `/login`              | Public     | Login page               |
| `/register`           | Public     | Registration page        |
| `/dashboard`          | Any role   | Dashboard with stats     |
| `/books`              | Any role   | Browse & search books    |
| `/books/add`          | Admin only | Add new book             |
| `/books/edit/{id}`    | Admin only | Edit book                |
| `/books/delete/{id}`  | Admin only | Delete book              |
| `/issues`             | Admin only | View all issued books    |
| `/issues/new`         | Admin only | Issue book to user       |
| `/issues/return/{id}` | Admin only | Return a book            |
| `/issues/my`          | User only  | My issued books          |
| `/admin/users`        | Admin only | All registered users     |
| `/logout`             | Any        | Log out                  |

---

## 🧪 POSTMAN API TESTING

### Login (get session cookie)
```
POST http://localhost:8080/login
Body (form-data):
  email    = admin@library.com
  password = admin123
```

### Get All Books
```
GET http://localhost:8080/books
```

### Add a Book (Admin session required)
```
POST http://localhost:8080/books/add
Body (form-data):
  title    = Spring Boot Guide
  author   = Josh Long
  category = Technology
  quantity = 5
```

### Issue a Book
```
POST http://localhost:8080/issues/new
Body (form-data):
  userId = 2
  bookId = 1
```

### Return a Book
```
GET http://localhost:8080/issues/return/1
```

---

## 💡 KEY CONCEPTS FOR VIVA

### Q: What is Spring MVC?
**A:** A pattern where Controller receives request → calls Service → Service calls Repository → returns data to View (HTML template).

### Q: What is JPA / Hibernate?
**A:** JPA is a specification for ORM (Object Relational Mapping). Hibernate is its implementation. It maps Java classes to database tables automatically.

### Q: What is Spring Security?
**A:** A framework for authentication (who are you?) and authorization (what can you do?). We use BCrypt for password hashing and role-based access control.

### Q: Why use DTO?
**A:** Data Transfer Objects carry data between layers without exposing entities. Useful for validation and security.

### Q: What is @Transactional?
**A:** Ensures a group of DB operations either ALL succeed or ALL fail together (atomicity). Prevents partial updates.

### Q: How is fine calculated?
**A:** Due date = issue date + 14 days. If returned late: Fine = extra days × ₹2/day.

---

## ⚙️ TECH STACK
- **Java 17** + **Spring Boot 3.2**
- **Spring MVC** (web layer)
- **Spring Data JPA** + **Hibernate** (database)
- **Spring Security** (auth + roles)
- **Thymeleaf** (HTML templates)
- **MySQL 8** (database)
- **Bootstrap 5** + **Bootstrap Icons** (UI)
- **Lombok** (reduces boilerplate)
- **Maven** (build tool)

---

*Library Management System — University Project*
