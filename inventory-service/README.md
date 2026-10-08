# Inventory Service

The Inventory Service manages stock levels for products in the platform. It is responsible for checking available quantity and lowering stock when an order is placed.

## What it does

- Lists available inventory items
- Checks product availability by product ID
- Reduces stock when an order is processed
- Uses Redis cache to speed up reads

## Port

- `8084`

## Before you start

This service also depends on Redis. Start Redis first:

```bash
docker run --name api-platform-redis -p 6379:6379 -d redis:7-alpine
```

If needed, configure:

```bash
export REDIS_HOST=localhost
export REDIS_PORT=6379
```

## Start locally

```bash
cd inventory-service
./mvnw spring-boot:run
```

## API endpoints

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/inventory` | Get all stock items |
| `GET` | `/inventory/{productId}` | Get availability for a product |
| `PUT` | `/inventory/{productId}/reduce?quantity={value}` | Reduce available stock |

## Example request

```bash
curl -X PUT "http://localhost:8084/inventory/1001/reduce?quantity=2"
```

## Access through the gateway

```bash
curl http://localhost:9090/inventory
curl -X PUT "http://localhost:9090/inventory/1001/reduce?quantity=2"
```

## Business rules

- Quantity to reduce must be greater than zero
- The service validates requested stock updates before continuing
- Successful stock reduction clears cached inventory results

## Redis cache behavior

- Inventory lookups are cached for a short period
- Stock reduction clears cached entries to reflect the latest availability
- Cache settings can be adjusted using `REDIS_HOST` and `REDIS_PORT`

## Notes

This service registers with Eureka and is exposed through the gateway under `/inventory/**`. It is designed to support retry and resilience patterns during transient backend issues.

## Troubleshooting

- If stock requests fail, confirm Redis is running.
- If the service is unreachable through the gateway, confirm Eureka is up.
- If quantity validation fails, make sure your request uses a positive number.