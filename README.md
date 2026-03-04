# E-Commerce Microservices

A microservices-based e-commerce platform built with **Spring Boot 3**, **Spring Cloud 2025.1.0**, and **Java 21**.

## Architecture

![Architecture Diagram](resourses/photo_2026-03-04_14-57-45.jpg)

The system follows a microservices architecture with:

- **API Gateway** — Single entry point routing to internal services
- **Config Server** — Centralized configuration backed by Git
- **Eureka Server** — Service discovery and registration
- **Kafka** — Async messaging for payment confirmations and order notifications
- **Zipkin** — Distributed tracing across services

### Services

| Service | Port | Database | Description |
|---------|------|----------|-------------|
| Config Server | 8888 | — | Centralized configuration |
| Discovery Service | 8761 | — | Eureka service registry |
| Customer | 8090 | MongoDB | Customer management |
| Product | 8050 | PostgreSQL | Product catalog & inventory |
| Order | TBD | PostgreSQL | Order processing |
| Payment | TBD | PostgreSQL | Payment handling |
| Notification | TBD | MongoDB | Email notifications via Kafka |

## Tech Stack

- **Java 21** / **Spring Boot 3** / **Spring Cloud 2025.1.0**
- **Spring Cloud Config** — Externalized configuration
- **Netflix Eureka** — Service discovery
- **PostgreSQL** — Relational data (Product, Order, Payment)
- **MongoDB** — Document data (Customer, Notification)
- **Flyway** — Database migrations
- **Docker Compose** — Infrastructure orchestration

## Getting Started

### Prerequisites

- Java 21+
- Maven 3.9+
- Docker & Docker Compose

### 1. Start Infrastructure

```bash
docker-compose up -d
```

This starts PostgreSQL, MongoDB, pgAdmin, Mongo Express, and MailDev.

### 2. Start Services (in order)

```bash
# 1. Config Server (must start first)
mvn clean package -f services/config-server/pom.xml -DskipTests
java -jar services/config-server/target/config-server-0.0.1-SNAPSHOT.jar

# 2. Discovery Service
mvn clean package -f services/discovery-service/pom.xml -DskipTests
java -jar services/discovery-service/target/discovery-service-0.0.1-SNAPSHOT.jar

# 3. Customer Service
mvn clean package -f services/customer/pom.xml -DskipTests
java -jar services/customer/target/customer-0.0.1-SNAPSHOT.jar

# 4. Product Service
mvn clean package -f services/product/pom.xml -DskipTests
java -jar services/product/target/product-0.0.1-SNAPSHOT.jar
```

### 3. Verify

- Eureka Dashboard: http://localhost:8761
- Config Server: http://localhost:8888

## API Endpoints

### Customer Service

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/customers` | Create a customer |
| PUT | `/api/v1/customers` | Update a customer |
| GET | `/api/v1/customers` | Get all customers |
| GET | `/api/v1/customers/{id}` | Get customer by ID |
| GET | `/api/v1/customers/exists/{id}` | Check if customer exists |
| DELETE | `/api/v1/customers/{id}` | Delete a customer |

### Product Service

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/products` | Create a product |
| POST | `/api/v1/products/purchase` | Purchase products (deducts inventory) |
| GET | `/api/v1/products` | Get all products |
| GET | `/api/v1/products/{id}` | Get product by ID |

## Infrastructure UIs

| Service | URL |
|---------|-----|
| Eureka Dashboard | http://localhost:8761 |
| pgAdmin | http://localhost:5050 |
| Mongo Express | http://localhost:8081 |
| MailDev | http://localhost:1080 |

## Project Structure

```
services/
├── config-server/          # Centralized configuration server
├── discovery-service/      # Eureka service registry
├── customer/               # Customer microservice (MongoDB)
└── product/                # Product microservice (PostgreSQL + Flyway)
```

