# Order Service

The Order Service handles customer orders and coordinates the order flow in the platform.

## What it does

- List existing orders
- Fetch one order by ID
- Create new orders
- Validate user and product information before confirming an order

## Port

- `8082`

## Start locally

```bash
cd order-service
./mvnw spring-boot:run
```

## API endpoints

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/orders` | Get all orders |
| `GET` | `/orders/{id}` | Get an order by ID |
| `POST` | `/orders` | Create a new order |

## Example request

```bash
curl -X POST http://localhost:8082/orders \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "productId": 1001,
    "quantity": 2
  }'
```

## Access through the gateway

Once the gateway is running, call:

```bash
curl http://localhost:9090/orders
curl -X POST http://localhost:9090/orders \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "productId": 1001,
    "quantity": 2
  }'
```

## Validation rules

The service checks that:

- `userId` is present
- `productId` is present
- `quantity` is valid and positive

## Notes

This service is part of the business flow and can interact with other services for validation and payment processing. It registers with Eureka and is exposed through the gateway at `/orders/**`.

## Troubleshooting

- If requests fail, confirm `eureka-server` is running.
- If the gateway cannot find the service, check the Eureka dashboard.
- If order creation fails, make sure the provided IDs and quantity are valid.