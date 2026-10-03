# E-Commerce API Gateway

The API Gateway is the public entry point for the platform. It receives client requests on a single port and forwards them to the appropriate microservice using Spring Cloud Gateway and Eureka-based service discovery.

## Responsibilities

- Routes HTTP traffic to backend services
- Aggregates the platform behind one endpoint
- Adds retry policies for downstream calls
- Limits incoming traffic with a Resilience4j rate limiter
- Centralizes access to the microservice ecosystem

## Port

- `9090`

## Route mappings

| Route | Target service |
| --- | --- |
| `/users/**` | `user-service` |
| `/orders/**` | `order-service` |
| `/payments/**` | `payment-service` |
| `/inventory/**` | `inventory-service` |

## Examples

```bash
curl http://localhost:9090/users
curl http://localhost:9090/orders
curl http://localhost:9090/payments
curl http://localhost:9090/inventory
```

## Run locally

```bash
cd ecom-api-gateway
./mvnw spring-boot:run
```

## Notes

The gateway depends on Eureka being available. Start the registry first, then bring up the gateway and downstream services.

For retries, routes to payment and inventory include configuration for transient failures, including timeout and server-side retry scenarios.

## Rate limiting

The gateway allows up to 100 requests per 1-second refresh period across all routes and clients for each gateway instance. Excess requests receive HTTP `429 Too Many Requests`. The limit is shared globally rather than tracked separately by client.

Configure the limit in `src/main/resources/application.yaml`:

```yaml
gateway:
  rate-limit:
    limit-for-period: 100
    limit-refresh-period: 1s
```

`limit-refresh-period` accepts a Spring duration value, such as `500ms`, `1s`, or `1m`.