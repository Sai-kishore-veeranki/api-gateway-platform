# User Service

The User Service manages customer information for the ecommerce platform.

## Purpose

- Register new users
- Fetch all users
- Fetch a single user by ID
- Provide user data to other services such as orders and payments

## Port

- `8081`

## API endpoints

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/users` | Return all users |
| `GET` | `/users/{id}` | Return a single user by ID |
| `POST` | `/users` | Create a new user |

## Example request

```bash
curl -X POST http://localhost:8081/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Jane Doe",
    "email": "jane@example.com"
  }'
```

## Run locally

```bash
cd user-service
./mvnw spring-boot:run
```

## Redis cache

Start Redis before this service. User list and profile reads are cached in Redis;
creating a user evicts both caches. List entries expire after 5 minutes and
individual profiles after 10 minutes. Configure the connection with `REDIS_HOST`
and `REDIS_PORT` (defaults: `localhost:6379`).

## Validation

The `POST /users` endpoint validates:

- `name` must not be blank
- `email` must be a valid email format

The service registers itself with Eureka and is discoverable via the gateway under `/users/**`.