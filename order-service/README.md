# Order Service

The Order Service handles order creation and retrieval for the ecommerce workflow.

## Purpose

- List orders
- Retrieve an order by ID
- Create a new order
- Coordinate communication with related services for validation and payment workflows

## Port

- `8082`

## API endpoints

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/orders` | Return all orders |
| `GET` | `/orders/{id}` | Return one order by ID |
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

## Run locally

```bash
cd order-service
./mvnw spring-boot:run
```

## Notes

The service uses Spring Cloud support for service-to-service interaction and is exposed through the API gateway at `/orders/**`.

`POST /orders` validates `userId`, `productId`, and `quantity` and returns an order payload describing status and totals.