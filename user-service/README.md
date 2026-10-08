# User Service

The User Service manages customer data for the platform. It stores users and exposes them to the rest of the system through the API gateway.

## What it does

- Create new users
- Read all users
- Read a single user by ID
- Cache user data in Redis for faster reads

## Port

- `8081`

## Before you start

This service depends on Redis. Make sure Redis is running before launching it.

```bash
docker run --name api-platform-redis -p 6379:6379 -d redis:7-alpine
```

If Redis is not running on `localhost:6379`, set the environment variables:

```bash
export REDIS_HOST=localhost
export REDIS_PORT=6379
```

## Start locally

```bash
cd user-service
./mvnw spring-boot:run
```

## API endpoints

| Method | Endpoint | Description |
| --- | --- | --- |
| `GET` | `/users` | Get all users |
| `GET` | `/users/{id}` | Get one user by ID |
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

## Access through the gateway

Once the gateway is running, use:

```bash
curl http://localhost:9090/users
curl http://localhost:9090/users/1
```

## Data validation

When creating a user:

- `name` cannot be blank
- `email` must be a valid email format

## Redis cache behavior

- User list reads are cached in Redis
- Individual profile reads are cached too
- Creating a user clears the relevant cache entries
- Cache expiration is set for a few minutes to keep data fresh

## Troubleshooting

- If the app fails to start, confirm Redis is reachable.
- If you cannot access it through the gateway, verify `eureka-server` is running and the service was registered.
- If you get validation errors, check that the request JSON includes a valid name and email.