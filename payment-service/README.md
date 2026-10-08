# Payment Service

The Payment Service handles payment-related workflows and is used to simulate how downstream services behave during a normal order flow.

## What it does

- Lists payment records
- Fetches a payment by ID
- Processes a new payment request
- Includes a flaky endpoint for resilience testing

## Port

- `8083`

## Start locally

```bash
cd payment-service
./mvnw spring-boot:run
```

## API endpoints

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/payments` | Get all payment records |
| `GET` | `/payments/{id}` | Get one payment by ID |
| `GET` | `/payments/flaky` | Simulate a temporary failure |
| `POST` | `/payments` | Create a payment |

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

## Access through the gateway

```bash
curl http://localhost:9090/payments
curl http://localhost:9090/payments/flaky
```

## Flaky endpoint

The `GET /payments/flaky` endpoint is intentionally unstable. It helps demonstrate retry and resilience behavior when requests fail temporarily.

## Notes

This service registers with Eureka and is available through the gateway under `/payments/**`. It also exposes actuator endpoints for health and metrics.

## Troubleshooting

- If the service does not appear in Eureka, make sure the registry is running.
- If the flaky endpoint fails often, that is expected for testing retries.
- If payment processing fails, check whether the request contains a valid order ID, user ID, and amount.