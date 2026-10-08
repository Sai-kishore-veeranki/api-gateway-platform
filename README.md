# API Gateway Platform

This repository is a beginner-friendly microservices demo built with Spring Boot and Spring Cloud. It shows how a typical e-commerce platform can be split into small services, registered in a discovery server, and accessed through a single API gateway.

## What this project demonstrates

- Service discovery with Netflix Eureka
- API routing through Spring Cloud Gateway
- Independent backend services for users, orders, payments, and inventory
- Redis-based caching in selected services
- Rate limiting and retry behavior at the gateway

## Service overview

| Service | Port | Purpose |
| --- | ---: | --- |
| `eureka-server` | 8761 | Service registry and dashboard |
| `user-service` | 8081 | Customer/user management |
| `order-service` | 8082 | Order creation and status flows |
| `payment-service` | 8083 | Payment processing |
| `inventory-service` | 8084 | Stock and inventory management |
| `ecom-api-gateway` | 9090 | Entry point for all client requests |

## Architecture

```text
Client
  |
  v
API Gateway (9090)
  |
  +--> user-service (8081)
  +--> order-service (8082)
  +--> payment-service (8083)
  +--> inventory-service (8084)
  |
  v
Eureka Server (8761)
```

## Prerequisites

Before starting the platform, make sure you have:

- Java 17 or newer
- Maven (or use the included `mvnw` wrapper)
- Docker (recommended for Redis) or a local Redis installation

## Quick start

The easiest way to run everything locally is:

1. Start Redis
2. Start Eureka
3. Start the backend services
4. Start the gateway
5. Test the routes through `http://localhost:9090`

### 1) Start Redis

```bash
docker run --name api-platform-redis -p 6379:6379 -d redis:7-alpine
```

If you already have Redis installed locally, you can skip Docker and use your existing Redis instance on `localhost:6379`.

### 2) Start the discovery server

```bash
cd eureka-server
./mvnw spring-boot:run
```

Then open:

```text
http://localhost:8761
```

This is the Eureka dashboard. All services will register here.

### 3) Start the application services

Open separate terminals and run each service:

```bash
cd user-service
./mvnw spring-boot:run
```

```bash
cd order-service
./mvnw spring-boot:run
```

```bash
cd payment-service
./mvnw spring-boot:run
```

```bash
cd inventory-service
./mvnw spring-boot:run
```

### 4) Start the API gateway

```bash
cd ecom-api-gateway
./mvnw spring-boot:run
```

### 5) Test the gateway

Sample requests:

```bash
curl http://localhost:9090/users
curl http://localhost:9090/orders
curl http://localhost:9090/payments
curl http://localhost:9090/inventory
```

## Route map

| Gateway path | Target service |
| --- | --- |
| `/users/**` | `user-service` |
| `/orders/**` | `order-service` |
| `/payments/**` | `payment-service` |
| `/inventory/**` | `inventory-service` |

## Important configuration notes

- `user-service` and `inventory-service` use Redis cache.
- If Redis is not running on `localhost:6379`, set these environment variables before starting the service:

```bash
export REDIS_HOST=localhost
export REDIS_PORT=6379
```

- The gateway has a simple rate limiter configured to prevent too many requests in a short time.

## Project structure

```text
api-gateway-platform/
├── README.md
├── eureka-server/
├── user-service/
├── order-service/
├── payment-service/
├── inventory-service/
├── ecom-api-gateway/
└── .gitignore
```

## Troubleshooting

### Service not showing in Eureka

- Make sure `eureka-server` is running first.
- Check the application logs for startup errors.
- Verify the service is using the correct Eureka client configuration.

### Redis connection errors

- Start Redis with Docker or local service.
- Check if the port is blocked.
- Make sure `REDIS_HOST` and `REDIS_PORT` point to the correct server.

### Gateway returns connection errors

- Confirm the gateway app is running on port `9090`.
- Ensure backend services are up and have registered with Eureka.
- Review the logs for route or discovery issues.

## Service-specific docs

- `eureka-server/README.md`
- `user-service/README.md`
- `order-service/README.md`
- `payment-service/README.md`
- `inventory-service/README.md`
- `ecom-api-gateway/README.md`

## Notes

This project is designed for learning and local development. It is not a production-ready deployment setup, but it is a clear example of how microservice communication and discovery can be structured in a Spring Cloud application.
