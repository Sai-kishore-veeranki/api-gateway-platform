# Eureka Server

Eureka Server is the discovery layer for the platform. Every Spring Boot service registers itself here so the API gateway can discover them through service names such as `user-service` and `order-service`.

## Role

- Maintains the service registry
- Exposes the Eureka dashboard for health and instance visibility
- Allows inter-service discovery without hardcoded IPs or ports

## Port

- `8761`

## Dashboard

Open the following URL in a browser:

```text
http://localhost:8761
```

## Run locally

```bash
cd eureka-server
./mvnw spring-boot:run
```

## Important configuration

The application registers itself as:

```yaml
spring:
  application:
    name: eureka-server
```

And discovery is disabled for the Eureka server itself:

```yaml
eureka:
  client:
    register-with-eureka: false
    fetch-registry: false
```

This is the first service that should be started before the rest of the platform.