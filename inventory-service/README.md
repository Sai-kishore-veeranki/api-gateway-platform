# Inventory Service

The Inventory Service manages product stock and availability across the platform.

## Purpose

- List inventory items
- Fetch item details by product ID
- Reduce stock quantities for an order
- Support resilience and retry patterns during stock checks

## Port

- `8084`

## API endpoints

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/inventory` | Return all inventory items |
| `GET` | `/inventory/{productId}` | Get product availability |
| `PUT` | `/inventory/{productId}/reduce?quantity={value}` | Reduce product stock |

## Example request

```bash
curl -X PUT "http://localhost:8084/inventory/1001/reduce?quantity=2"
```

## Run locally

```bash
cd inventory-service
./mvnw spring-boot:run
```

## Redis cache

Start Redis before this service. Inventory list and product lookups are cached
for 30 seconds, and a successful stock reduction evicts both caches. Configure
the connection with `REDIS_HOST` and `REDIS_PORT` (defaults: `localhost:6379`).

## Notes

This service registers with Eureka and is exposed through the gateway at `/inventory/**`.

Stock updates are validated with positive quantity checks, and the controller is prepared for retry behavior in case transient backend issues occur.