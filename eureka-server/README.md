# Eureka Server

This service is the registry for the whole platform. Every microservice registers itself here so the API gateway can discover them by name instead of hardcoded URLs.

## What it does

- Keeps track of all running services
- Shows a dashboard of live service instances
- Lets other services discover each other automatically

## Port

- `8761`

## Why it matters

The API gateway and all microservices rely on Eureka for finding service locations. If this service is not running, the rest of the platform may fail to discover each other.

## Start it locally

```bash
cd eureka-server
./mvnw spring-boot:run
```

Then open the dashboard:

```text
http://localhost:8761
```

## Typical startup order

Start this service first before the others:

1. `eureka-server`
2. `user-service`
3. `order-service`
4. `payment-service`
5. `inventory-service`
6. `ecom-api-gateway`

## Important configuration

This application identifies itself as:

```yaml
spring:
  application:
    name: eureka-server
```

And it does not register itself with its own registry:

```yaml
eureka:
  client:
    register-with-eureka: false
    fetch-registry: false
```

## Troubleshooting

- If the dashboard does not open, check whether the app started successfully.
- If no services appear, make sure each service is running and registered.
- If the port is already used, stop the conflicting process or change the port in the app settings.