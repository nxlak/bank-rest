# bank-rest

A Spring Boot RESTful API for managing bank cards and transactions.

## Features

* User registration and authentication with JWT
* Role-based access control (ADMIN, USER)
* Card management: create, update status, block, delete, list
* Balance inquiry and funds transfer between cards
* OpenAPI/Swagger UI integration
* Database migrations with Liquibase
* Docker and Docker Compose setup

## Getting Started

1. **Clone the repository**

   ```bash
   git clone https://github.com/nxlak/bank-rest
   cd bank-rest
   ```
   
2. **Run with Docker Compose**

      ```bash
   docker compose up --build
   ```

   * API will be available at `http://localhost:8080`
   * Swagger UI at `http://localhost:8080/swagger-ui.html`

3. **Database migrations**

   Liquibase changelogs are defined under `src/main/resources/db/changelog`.

## API Documentation

Interactive API docs via Swagger UI:

```
http://localhost:8080/swagger-ui.html
```

## Testing

Unit tests for business logic are located under `src/test/java`. Run all tests with:

```bash
./mvnw test
```
