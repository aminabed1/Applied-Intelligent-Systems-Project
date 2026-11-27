# 📚 Library Management System

A RESTful backend service built with **Spring Boot** for comprehensive management of library books and borrowing workflows. This project is designed following a clean layered architecture, centralized exception handling, dynamic search capabilities, and seamless containerized deployment using Docker.

## 🛠 Technologies Used

* **Programming Language:** Java 21 (Eclipse Temurin)
* **Core Framework:** Spring Boot 3.x (Spring Web, Spring Data JPA, Spring Validation)
* **Database:** PostgreSQL 18
* **Caching Infrastructure:** Redis 7
* **Build & Dependency Management:** Apache Maven
* **Containerization:** Docker & Docker Compose
* **Utilities:** Lombok, Jakarta Bean Validation

## 📋 Prerequisites

Depending on how you choose to run the application, ensure you have the following installed:

**Option 1: Running with Docker (Recommended)**
* [Docker](https://docs.docker.com/get-docker/)
* [Docker Compose](https://docs.docker.com/compose/install/)

**Option 2: Running Locally**
* [JDK 21](https://adoptium.net/)
* [Apache Maven 3.8+](https://maven.apache.org/)
* Running instances of PostgreSQL and Redis

## 🚀 How to Run the Project

### 1. Running with Docker Compose (Quickest Method)

This approach automatically builds and links all required services (the Spring Boot application, PostgreSQL database, and Redis).

1. Build the executable JAR file using Maven (required for the `Dockerfile` copy step):
   ```bash
   ./mvnw clean package -DskipTests
   ```
   *(On Windows, use `mvnw.cmd clean package -DskipTests`)*

2. Build and start the containers in detached mode:
   ```bash
   docker-compose up -d --build
   ```

3. The API will be accessible at `http://localhost:9091`.
   * PostgreSQL is exposed on host port `5434`.
   * Redis is exposed on host port `6380`.

### 2. Running Locally (Without Docker)

1. Ensure a PostgreSQL instance is running with a database named `lib_db`.
2. Update the datasource and Redis properties in `src/main/resources/application.properties` (or `application.yml`) to match your local environment.
3. Run the application using the Maven wrapper:
   ```bash
   ./mvnw spring-boot:run
   ```

## 📁 Project Structure

The project follows a standard **Layered Architecture** to maintain a clear separation of concerns:

```text
ut.ac.ir.library
├── controller/                  # Presentation Layer (REST Controllers handling HTTP requests)
│   ├── BookController.java      # Endpoints for book CRUD operations and search
│   └── LoanController.java      # Endpoints for borrowing and returning books
├── dto/                         # Data Transfer Objects
│   ├── request/                 # Incoming request payloads with Jakarta validation rules
│   │   ├── EditBookInfoRequest.java
│   │   ├── InsertBookRequest.java
│   │   └── LoanRequest.java
│   └── response/                # Standardized API response wrappers
│       └── ApiResponse.java
├── entity/                      # Database Entities (JPA)
│   ├── Book.java                # Maps to the 'books' table
│   └── Loan.java                # Maps to the 'loans' table
├── exception/                   # Centralized Error Handling
│   ├── custom/                  # Domain-specific runtime exceptions
│   │   ├── ConflictException.java
│   │   └── NotFoundException.java
│   └── GlobalExceptionHandler.java # Global @RestControllerAdvice formatting errors into ApiResponse
├── repository/                  # Data Access Layer (Spring Data JPA Repositories)
│   ├── BookRepository.java
│   └── LoanRepository.java
└── service/                     # Business Logic Layer
    ├── BookService.java         # Core business logic for books
    ├── BookSpecification.java   # Dynamic query builder using JPA Criteria API
    └── LoanService.java         # Core business logic for loans, availability checks, and returns
```

## 🔌 API Endpoints Overview

### Book Management (`/api/books`)

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/books/insert` | Register a new book with ISBN format and publish year validation |
| `GET` | `/api/books/get-all` | Retrieve a paginated list of books with optional filters (`title`, `author`, `publishYear`, `isAvailable`) |
| `GET` | `/api/books/{id}` | Retrieve details of a specific book by its ID |
| `PATCH` | `/api/books/{id}` | Partially update book metadata (title, author, publish year) |
| `DELETE` | `/api/books/delete/{id}` | Soft-delete a book from the library catalog |

### Loan Management (`/api/loans`)

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `PUT` | `/api/loans/insert` | Issue a new loan for an available book |
| `PATCH` | `/api/loans/return/{id}` | Process a book return (by book ID) and mark the book as available again |

## 💡 Three Key Architectural Decisions

### 1. Soft Delete Pattern for Books
Instead of permanently removing a record from the database when a book is deleted, an `isDeleted` boolean flag is toggled on the `Book` entity.
* **Rationale:** Books maintain historical relationships with the `loans` table. Hard-deleting a book would either violate foreign key constraints or erase historical borrowing records. Combining soft deletion with an automatic filter in `BookSpecification` preserves data integrity while hiding deleted books from active catalog searches.

### 2. Dynamic Querying with JPA Specification
For the book search and filtering endpoint (`/api/books/get-all`), `JpaSpecificationExecutor` and a dedicated `BookSpecification` class were chosen over defining numerous combinatorial derived query methods in `BookRepository`.
* **Rationale:** Supporting four optional filter parameters (title, author, publish year, availability) alongside pagination (`Pageable`) requires dynamic query construction at runtime. Using the Criteria API keeps the repository interface clean, avoids method explosion, and makes adding new search criteria effortless.

### 3. Standardized `ApiResponse<T>` Wrapper & Global Exception Handling
Every endpoint—whether successful or failing due to validation errors, missing resources (`404`), or state conflicts (`409`)—returns a consistent generic `ApiResponse<T>` envelope managed centrally via `@RestControllerAdvice`.
* **Rationale:** API consumers (such as frontend clients or mobile apps) benefit from a predictable JSON contract containing `success`, `status`, `message`, `timestamp`, and `data`/`errors` fields. This simplifies client-side parsing and error handling.

## 🔮 Future Improvements & Fixes (Given More Time)

If additional development time were available, the following enhancements and bug fixes would be prioritized:

1. **Leverage Redis for Caching:** The Redis container is already provisioned in `docker-compose.yml`. The next step is integrating Spring Cache (`@Cacheable`, `@CacheEvict`) to cache frequent book lookups and paginated search results, reducing load on PostgreSQL.
2. **Interactive API Documentation (Swagger / OpenAPI):** Integrate `springdoc-openapi` to automatically generate interactive Swagger UI documentation for easier testing and onboarding.
3. **Member Management, Authentication & Late Fee Calculation:** Currently, `borrowerName` is stored as a plain string. A full-scale version would introduce a `Member`/`User` entity, secure endpoints using **Spring Security & JWT**, and implement automated overdue fee calculation when `actualReturnDate` exceeds `returnDate`.
4. **Bug Fixes & REST Convention Refinements:**
   * Fix the typo in `BookController` (`@RequestParam String tile` $\rightarrow$ `title`) and align the field names in `BookSpecification` (`deleted` $\rightarrow$ `isDeleted`, `available` $\rightarrow$ `isAvailable`) to match the `Book` entity attributes.
   * Add `@Builder.Default` to `isAvailable = true` in the `Book` entity so Lombok's builder does not default the primitive boolean to `false`.
   * Add null checks in `BookService.editInfo` so `PATCH` requests only overwrite fields that are explicitly provided rather than setting omitted fields to `null`, and change `LoanController.insert` from `@PutMapping` to `@PostMapping` to adhere strictly to REST semantics.
   * Expand the test suite with **Testcontainers** for end-to-end integration testing against a real PostgreSQL instance.