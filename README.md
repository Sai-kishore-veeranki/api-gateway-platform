# API Gateway Platform

This repository is a hands-on Spring Boot and Spring Cloud example for building a small microservices-based e-commerce platform. It demonstrates a common pattern in real applications:

- multiple independent services
- service registration and discovery
- a single API entry point through an API gateway
- central routing and request management
- caching, logging, and simple resilience patterns

The goal is to keep the architecture easy to understand for learning, experimentation, and local development.

## What this project demonstrates

This project shows how to build a distributed system using the following patterns:

- Service discovery with Netflix Eureka
- API routing with Spring Cloud Gateway
- Decoupled backend services for users, orders, payments, and inventory
- Redis caching for performance in selected services
- AOP-based method logging to capture execution time and failures
- Gateway-level rate limiting and retry handling

## Business flow

A request from a client goes through a single gateway and is forwarded to the correct service. The services register themselves with Eureka so the gateway can discover them and route traffic without hardcoded host and port configuration.

```text
Client
  |
  v
API Gateway (9090)
  |
  +--> User Service (8081)
  +--> Order Service (8082)
  +--> Payment Service (8083)
  +--> Inventory Service (8084)
  |
  v
Eureka Server (8761)
```

This architecture is useful when different parts of the application need to grow independently without forcing all business logic into one monolithic application.

## Service overview

| Service | Port | Responsibility |
| --- | ---: | --- |
| `eureka-server` | 8761 | Registers and tracks all microservices |
| `user-service` | 8081 | Manages users and customer data |
| `order-service` | 8082 | Handles order creation and status updates |
| `payment-service` | 8083 | Processes payment operations |
| `inventory-service` | 8084 | Tracks stock and inventory levels |
| `ecom-api-gateway` | 9090 | Acts as the public entry point for client requests |

## Architecture summary

The platform follows a typical microservice layout:

- Clients talk to one public endpoint: the gateway.
- The gateway routes incoming requests to the correct service.
- All services register with Eureka, which acts as the service registry.
- Backend services remain mostly independent and can be developed, tested, or scaled separately.

## Prerequisites

Before starting the platform, make sure the following are installed:

- Java 17 or newer
- Maven, or use the provided Maven wrapper if available
- Docker (recommended for Redis) or a local Redis installation

## Quick start

The fastest way to launch the full platform locally is with Docker Compose:

```bash
docker compose up --build
```

This command starts Redis, the discovery server, all application services, and the API gateway in one step. After the services are running, test the system through `http://localhost:9090`.

### Manual startup

If you want to start the platform manually, follow these steps:

1. Start Redis
2. Start Eureka
3. Start the backend services
4. Start the API gateway
5. Test the routes via the gateway

### 1) Start Redis

```bash
docker run --name api-platform-redis -p 6379:6379 -d redis:7-alpine
```

If Redis is already installed locally, you can use your existing instance on `localhost:6379` and skip Docker.

### 2) Start the discovery server

```bash
cd eureka-server
mvn spring-boot:run
```

Then open the Eureka dashboard here:

```text
http://localhost:8761
```

This dashboard shows the registered services and helps confirm that each service is healthy and connected.

### 3) Start the application services

Open separate terminals and run each service one by one:

```bash
cd user-service
mvn spring-boot:run
```

```bash
cd order-service
mvn spring-boot:run
```

```bash
cd payment-service
mvn spring-boot:run
```

```bash
cd inventory-service
mvn spring-boot:run
```

### 4) Start the API gateway

```bash
cd ecom-api-gateway
mvn spring-boot:run
```

Once the gateway starts, it will begin forwarding requests to the available backend services using the registered names in Eureka.

### 5) Test the gateway

You can verify the routing by calling the gateway endpoints:

```bash
curl http://localhost:9090/users
curl http://localhost:9090/orders
curl http://localhost:9090/payments
curl http://localhost:9090/inventory
```

These requests are routed through the gateway to the underlying services instead of exposing each service directly to clients.

## Route map

The gateway exposes a simple route structure for the microservices:

| Gateway path | Target service |
| --- | --- |
| `/users/**` | `user-service` |
| `/orders/**` | `order-service` |
| `/payments/**` | `payment-service` |
| `/inventory/**` | `inventory-service` |

The gateway acts as a single public API layer while the internal services remain separate.

## Important configuration notes

- `user-service` and `inventory-service` use Redis caching.
- If Redis is not available on `localhost:6379`, set the environment variables before starting the service:

```bash
export REDIS_HOST=localhost
export REDIS_PORT=6379
```

- The gateway includes rate limiting to reduce abuse and protect backend services from excessive traffic.

## Project structure

```text
api-gateway-platform/
├── README.md
├── docker-compose.yml
├── .gitignore
├── eureka-server/
├── user-service/
├── order-service/
├── payment-service/
├── inventory-service/
└── ecom-api-gateway/
```

You can view each service folder for its own README and module-specific details.

## Troubleshooting

### Service not appearing in Eureka

- Ensure `eureka-server` starts before the other services.
- Check the application logs for startup and configuration errors.
- Confirm each service is configured to register with the correct Eureka server.

### Redis connection errors

- Start Redis using Docker or a local instance.
- Make sure the port is reachable and not blocked by firewall rules.
- Verify that `REDIS_HOST` and `REDIS_PORT` match your environment.

### Gateway returns connection errors

- Confirm the gateway is running on port `9090`.
- Check whether the backend services are turned on and registered.
- Review gateway and service logs for route or discovery issues.

## Service-specific documentation

Each module may contain its own README with more focused implementation details:

- `eureka-server/README.md`
- `user-service/README.md`
- `order-service/README.md`
- `payment-service/README.md`
- `inventory-service/README.md`
- `ecom-api-gateway/README.md`

## Notes

This repository is intended for learning and local experimentation. It is not a production deployment setup, but it is a clear and practical example of how microservice discovery, routing, and communication can be organized in a Spring Cloud application.

If you are new to microservices, this project is a good starting point for understanding how services communicate through a gateway instead of directly exposing every service to clients.
