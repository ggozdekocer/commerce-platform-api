# 🛒 E-Commerce Platform Backend API

An enterprise-ready RESTful backend service built with **Java**, **Spring Boot**, **Spring Security**, and **PostgreSQL**. Features JWT authentication, role-based authorization, shopping cart operations, and Docker-based database administration.

## ✨ Key Features

- **Authentication & Security:** Secure JWT-based user session handling, protected API endpoints, and Role-Based Access Control (Admin / User).
- **Product & Category Management:** Dynamic product catalog with filtering, search capabilities, and category associations.
- **Shopping Cart & Checkout Flow:** Comprehensive shopping cart management, address handling, credit card/payment simulation, and order lifecycle operations.
- **Data Persistence:** Relational data architecture structured using **Spring Data JPA** & **PostgreSQL**.
- **Database Management:** Containerized **PgAdmin** / **PostgreSQL** setup using Docker for efficient database administration and monitoring.

## 🛠️ Tech Stack

- **Language & Framework:** Java 17+, Spring Boot
- **Database:** PostgreSQL with Spring Data JPA
- **Security:** Spring Security, JSON Web Tokens (JWT)
- **Database Tooling:** Docker, PgAdmin
- **Build Tool:** Maven

## ⚙️ Getting Started

### Prerequisites

- Java 17 or higher
- PostgreSQL & PgAdmin (via Docker or local installation)

### Local Setup

1. Clone the repository:
   git clone https://github.com/ggozdekocer/e-commerce-backend.git
   cd e-commerce-backend

2. Configure Database Connection:
   Update src/main/resources/application.properties with your PostgreSQL/PgAdmin credentials:
   spring.datasource.url=jdbc:postgresql://localhost:5432/ecommerce_db
   spring.datasource.username=your_username
   spring.datasource.password=your_password

3. Run PgAdmin / Database container with Docker:
   docker run -p 5050:80 -e PGADMIN_DEFAULT_EMAIL=admin@admin.com -e PGADMIN_DEFAULT_PASSWORD=admin --name pgadmin4 dpage/pgadmin4

4. Run the application:
   ./mvnw spring-boot:run

## 👤 Author

- **Gözde Koçer** - [LinkedIn](https://linkedin.com/in/gozdekocer) • [GitHub](https://github.com/ggozdekocer)
