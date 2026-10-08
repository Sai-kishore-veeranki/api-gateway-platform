# API Gateway

The API Gateway is the single front door for the entire platform. Instead of calling each service directly, clients send requests to one address: `http://localhost:9090`.

## What it does

- Routes incoming requests to the correct backend service
- Uses Eureka to discover services dynamically
- Adds retry handling for temporary failures
- Limits traffic to protect the platform from spikes

## Port

- `9090`

## Start locally

Make sure `eureka-server` is already running, then start the gateway:

```bash
cd ecom-api-gateway
./mvnw spring-boot:run
```

## Route map

| Gateway path | Target service |
| --- | --- |
| `/users/**` | `user-service` |
| `/orders/**` | `order-service` |
| `/payments/**` | `payment-service` |
| `/inventory/**` | `inventory-service` |

## Example requests

```bash
curl http://localhost:9090/users
curl http://localhost:9090/orders
curl http://localhost:9090/payments
curl http://localhost:9090/inventory
```

## Typical flow

1. Start Eureka
2. Start Redis if needed
3. Start the service apps
4. Start the gateway
5. Send requests through `http://localhost:9090`

## Rate limiting

The gateway uses Resilience4j to limit requests. It allows up to 100 requests per second per gateway instance. If the limit is exceeded, the gateway responds with HTTP `429 Too Many Requests`.

The limits are configured in `src/main/resources/application.yaml`.

```yaml
gateway:
  rate-limit:
    limit-for-period: 100
    limit-refresh-period: 1s
```

## Retry behavior

Routes to payment and inventory include retry logic for transient errors such as timeouts or temporary service failures.

## Troubleshooting

- If the gateway cannot route requests, make sure Eureka is running.
- If the endpoint returns 404, check the path and confirm the target service is up.
- If requests are rejected with `429`, the rate limiter may be configured too aggressively for your testing load.