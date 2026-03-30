# E-Commerce Microservices

A microservices-based e-commerce platform built with **Spring Boot 4.0.3**, **Spring Cloud 2025.1.0**, and **Java 21**.

## Architecture

![Architecture Diagram](resources/photo_2026-03-04_14-57-45.jpg)

The system follows a microservices architecture with:

- **API Gateway** — Single entry point routing all traffic to internal services via Eureka
- **Config Server** — Centralized configuration for all services
- **Eureka Server** — Service discovery and registration
- **Kafka** — Async messaging for order confirmations and payment notifications
- **Mail Dev** — Email testing for notification service

### Services

| Service | Port | Database | Description |
|---------|------|----------|-------------|
| Config Server | 8888 | — | Centralized configuration |
| Discovery Service | 8761 | — | Eureka service registry |
| Gateway | 8222 | — | API gateway (Spring Cloud Gateway) |
| Customer | 8090 | MongoDB | Customer management |
| Product | 8050 | PostgreSQL | Product catalog & inventory |
| Order | 8070 | PostgreSQL | Order processing & orchestration |
| Payment | 8060 | PostgreSQL | Payment handling |
| Notification | 8040 | MongoDB | Email notifications via Kafka |

### Communication

- **Synchronous:** REST via OpenFeign (Order → Customer, Order → Payment) and RestTemplate (Order → Product)
- **Asynchronous:** Kafka topics
  - `order-topic` — Order Service → Notification Service
  - `payment-topic` — Payment Service → Notification Service

## Tech Stack

- **Java 21** / **Spring Boot 4.0.3** / **Spring Cloud 2025.1.0**
- **Spring Cloud Config** — Externalized configuration
- **Spring Cloud Gateway** — API routing and load balancing
- **Netflix Eureka** — Service discovery
- **Apache Kafka** — Async event-driven messaging
- **OpenFeign** — Declarative REST clients
- **PostgreSQL** — Relational data (Product, Order, Payment)
- **MongoDB** — Document data (Customer, Notification)
- **Flyway** — Database migrations (Product Service)
- **Thymeleaf** — HTML email templates (Notification Service)
- **Redis** — Caching (Product Service)
- **Zipkin** — Distributed tracing
- **Docker** — Multi-stage builds for all services (Java 21 Alpine)
- **Docker Compose** — Full-stack orchestration (infrastructure + all services)

## Getting Started

### Prerequisites

- Java 21+
- Maven 3.9+
- Docker & Docker Compose

### Option A: Full Stack with Docker (Recommended)

```bash
# Build and start everything
docker compose up -d --build

# Check all services are running
docker compose ps

# View logs for a specific service
docker compose logs -f <service-name>

# Stop everything
docker compose down
```

Docker Compose handles the startup order automatically via healthchecks:
1. **config-server** starts first
2. **discovery-service** waits for config-server to be healthy
3. **Business services** wait for discovery-service to be healthy
4. **gateway** starts last (after all business services are up)

PostgreSQL databases (`order`, `payment`, `product`) are auto-created on first start.

### Option B: Local Development

Start only infrastructure, then run services manually:

```bash
# 1. Start infrastructure
docker compose up postgresql pgadmin mongodb mongo-express zookeeper kafka mail-dev zipkin redis -d

# 2. Build and run services (in order)
mvn clean package -f services/config-server/pom.xml -DskipTests
java -jar services/config-server/target/config-server-0.0.1-SNAPSHOT.jar

mvn clean package -f services/discovery-service/pom.xml -DskipTests
java -jar services/discovery-service/target/discovery-service-0.0.1-SNAPSHOT.jar

# Then start remaining services in any order:
# gateway, customer, product, order, payment, notification
mvn clean package -f services/<service-name>/pom.xml -DskipTests
java -jar services/<service-name>/target/<service-name>-0.0.1-SNAPSHOT.jar
```

### Verify

- Eureka Dashboard: http://localhost:8761 — all services should be registered
- Config Server: http://localhost:8888/actuator/health
- API Gateway: http://localhost:8222/api/v1/customers

## API Endpoints

All endpoints are accessible through the **API Gateway** at `http://localhost:8222`.

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

### Order Service

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/orders` | Create an order |
| GET | `/api/v1/orders` | Get all orders |
| GET | `/api/v1/orders/{order-id}` | Get order by ID |
| GET | `/api/v1/order-lines/order/{order-id}` | Get order lines by order ID |

### Payment Service

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/payments` | Create a payment |

### Notification Service

No REST endpoints — event-driven only. Consumes Kafka messages and sends emails.

## Infrastructure UIs

| Service | URL | Credentials |
|---------|-----|-------------|
| Eureka Dashboard | http://localhost:8761 | — |
| API Gateway | http://localhost:8222 | — |
| pgAdmin | http://localhost:5050 | pgadmin4@pgadmin.org / admin |
| Mongo Express | http://localhost:8081 | root / root |
| MailDev | http://localhost:1080 | — |
| Zipkin | http://localhost:9411 | — |

## Project Structure

```
services/
├── config-server/          # Centralized configuration server
├── discovery-service/      # Eureka service registry
├── gateway/                # API Gateway (Spring Cloud Gateway)
├── customer/               # Customer microservice (MongoDB)
├── product/                # Product microservice (PostgreSQL + Flyway + Redis cache)
├── order/                  # Order microservice (PostgreSQL + Kafka producer)
├── payment/                # Payment microservice (PostgreSQL + Kafka producer)
└── notification/           # Notification microservice (MongoDB + Kafka consumer + email)
docker/
└── postgres/
    └── init-databases.sql  # Auto-creates order, payment, product databases
```
