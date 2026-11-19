# Commerce Platform API

A RESTful backend for an online store, built with Java 17, Spring Boot 3, Spring Security and PostgreSQL.
It handles user registration and login with JWT tokens, a product catalog, saved addresses and payment cards,
and an order workflow that checks stock and rolls back safely when something goes wrong.

## Features

- **Authentication:** sign up, log in and verify a session with JWT tokens. Passwords are hashed with BCrypt.
- **Catalog:** browse categories and products (public endpoints).
- **Addresses and cards:** each logged-in user manages their own delivery addresses and saved cards.
- **Orders:** creating an order runs in one transaction. It checks stock, reduces stock, increases the sell count,
  calculates the total price on the server (the price sent by the client is ignored) and stores only the masked
  card number. If any product is missing or out of stock, nothing is saved.
- **Consistent responses:** every endpoint returns the same `CommonResponse` shape, and errors are mapped to
  clear HTTP status codes (400, 401, 404, 409) by a global exception handler.
- **API documentation:** interactive Swagger UI.

## Tech stack

- Java 17, Spring Boot 3.3
- Spring Web, Spring Data JPA, Spring Security with a custom JWT filter
- PostgreSQL
- Lombok, SLF4J logging
- springdoc-openapi (Swagger UI)
- JUnit 5 and Mockito for unit tests
- Maven, Docker (multi-stage build, runs as a non-root user)

## Project structure

The code is grouped by domain, under `tr.com.java.commerceplatform`:

```
auth/      login, signup, JWT filter and security configuration
user/      users and roles
product/   products and categories
order/     orders and the order workflow
address/   delivery addresses
card/      saved payment cards
store/     stores of store-owner users
common/    shared response type, exceptions, configuration and utilities
```

## Getting started

### Prerequisites

- JDK 17 (newer JDKs can break the Lombok version used here)
- Docker, for the PostgreSQL database

### 1. Clone the repository

```bash
git clone https://github.com/ggozdekocer/commerce-platform-api.git
cd commerce-platform-api
```

### 2. Start PostgreSQL

```bash
docker run --name postgres-db \
  -e POSTGRES_PASSWORD=your_password \
  -e POSTGRES_DB=ecommerce-db \
  -p 5432:5432 -d postgres:17
```

### 3. Set the environment variables

| Variable       | Required | Default                                         | Description                                     |
|----------------|----------|-------------------------------------------------|-------------------------------------------------|
| `JWT_SECRET`   | yes      | none                                            | Key used to sign tokens, at least 32 characters |
| `DB_URL`       | no       | `jdbc:postgresql://localhost:5432/ecommerce-db` | JDBC connection URL                             |
| `DB_USERNAME`  | no       | `postgres`                                      | Database user                                   |
| `DB_PASSWORD`  | yes      | see `application.properties`                    | Database password                               |

Generate a secret with:

```bash
openssl rand -base64 48
```

The application does not start without `JWT_SECRET`, so no key is ever stored in the code.

### 4. Run the application

```bash
export JWT_SECRET=your_generated_secret
export DB_PASSWORD=your_password
./mvnw spring-boot:run
```

The API is now available at `http://localhost:8080`.

### Run with Docker

```bash
docker build -t commerce-platform-api .
docker run -p 8080:8080 \
  -e JWT_SECRET=your_generated_secret \
  -e DB_PASSWORD=your_password \
  -e DB_URL=jdbc:postgresql://host.docker.internal:5432/ecommerce-db \
  commerce-platform-api
```

## API documentation

Swagger UI: `http://localhost:8080/swagger-ui/index.html`

JSON field names use **snake_case** (for example `role_id`, `card_no`).

### Endpoints

| Method | Path                 | Auth     | Description                                   |
|--------|----------------------|----------|-----------------------------------------------|
| POST   | `/signup`            | public   | Register a user, returns the user and a token |
| POST   | `/login`             | public   | Log in, returns the user and a token          |
| GET    | `/verify`            | required | Check the current token, returns a new one    |
| GET    | `/roles`             | public   | List the roles a user can sign up with        |
| GET    | `/categories`        | public   | List product categories                       |
| GET    | `/products`          | public   | List all products with the total count        |
| GET    | `/products/{id}`     | public   | Get one product                               |
| GET    | `/user/address`      | required | List my addresses                             |
| POST   | `/user/address`      | required | Add an address                                |
| DELETE | `/user/address/{id}` | required | Delete one of my addresses                    |
| GET    | `/user/card`         | required | List my saved cards                           |
| POST   | `/user/card`         | required | Add a card                                    |
| DELETE | `/user/card/{id}`    | required | Delete one of my cards                        |
| POST   | `/order`             | required | Create an order                               |
| GET    | `/order`             | required | List my orders                                |

Protected endpoints need the header `Authorization: Bearer <token>`.

### Response format

```json
{
  "data": {},
  "success_message": "Order created successfully",
  "http_status": "CREATED",
  "error_message": null
}
```

## Tests

```bash
./mvnw test
```

The tests use JUnit 5 and Mockito. They cover the order workflow (stock check, price calculation, rollback cases),
user signup and login, the product service and card number masking. The context test needs the PostgreSQL
container to be running.

## Notes

- Payment is simulated: the API stores orders and masked card numbers, and does not talk to a payment provider.
- Saved cards (`/user/card`) are stored for the demo and are not meant for real card data.

## Author

Gözde Koçer - [LinkedIn](https://linkedin.com/in/gozdekocer) • [GitHub](https://github.com/ggozdekocer)