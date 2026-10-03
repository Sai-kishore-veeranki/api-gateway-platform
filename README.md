# API Gateway Platform

This repository contains a Spring Boot + Spring Cloud microservices demo for an ecommerce-style platform. It uses a central API gateway and Netflix Eureka for service discovery.

## Services

| Service | Port | Purpose |
| --- | ---: | --- |
| `eureka-server` | 8761 | Service discovery dashboard and registry |
| `user-service` | 8081 | Customer profile management |
| `order-service` | 8082 | Order placement and status tracking |
| `payment-service` | 8083 | Payment processing |
| `inventory-service` | 8084 | Product stock management |
| `ecom-api-gateway` | 9090 | Unified entry point for all downstream services |

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

## Quick start

1. Start `eureka-server` first.
2. Start the remaining services in any order.
3. Call the gateway at `http://localhost:9090`.

Example:

```bash
cd eureka-server
./mvnw spring-boot:run

cd ../user-service
./mvnw spring-boot:run

cd ../order-service
./mvnw spring-boot:run

cd ../payment-service
./mvnw spring-boot:run

cd ../inventory-service
./mvnw spring-boot:run

cd ../ecom-api-gateway
./mvnw spring-boot:run
```

## Route map

- `/users/**` -> `user-service`
- `/orders/**` -> `order-service`
- `/payments/**` -> `payment-service`
- `/inventory/**` -> `inventory-service`

## Rate limiting

The API gateway uses Resilience4j to allow up to 100 requests per second across all routes and clients per gateway instance. Requests over the configured limit receive HTTP `429 Too Many Requests`. Adjust `gateway.rate-limit.limit-for-period` and `gateway.rate-limit.limit-refresh-period` in `ecom-api-gateway/src/main/resources/application.yaml` to change the limit.

## References

- `eureka-server/README.md`
- `user-service/README.md`
- `order-service/README.md`
- `payment-service/README.md`
- `inventory-service/README.md`
- `ecom-api-gateway/README.md`
