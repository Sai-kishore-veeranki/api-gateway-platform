# Payment Service

The Payment Service manages payment processing for ecommerce orders.

## Purpose

- Review payment records
- Fetch a payment by ID
- Trigger a payment workflow
- Provide a deliberately flaky endpoint for resilience testing scenarios

## Port

- `8083`

## API endpoints

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/payments` | Return all payments |
| `GET` | `/payments/{id}` | Return one payment by ID |
| `GET` | `/payments/flaky` | Simulate transient failures |
| `POST` | `/payments` | Process a payment |

## Example request

```bash
curl -X POST http://localhost:8083/payments \
  -H "Content-Type: application/json" \
  -d '{
    "orderId": 101,
    "userId": 1,
    "amount": 199.99
  }'
```

## Run locally

```bash
cd payment-service
./mvnw spring-boot:run
```

## Notes

This service is reachable through the gateway at `/payments/**` and is configured with actuator endpoints for health and metrics.

The `GET /payments/flaky` endpoint exists to test handling of transient downstream issues when used with the gateway retry configuration.