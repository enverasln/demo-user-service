# Demo User Service

Small Spring Boot 3 application used as a sandbox for the AI Development Agent.

## What it does

Exposes `POST /users/register` accepting a JSON body:

```json
{ "email": "alice@example.com", "password": "secret123" }
```

and returns:

```json
{ "id": 1, "email": "alice@example.com" }
```

Users are stored in an in-memory `Map` keyed by email.

## Run

```bash
mvn spring-boot:run
```

## Test

```bash
mvn test
```

## Known limitations (intentional)

- `POST /users/register` accepts **any** string as the `email` field — there is no
  format validation. Invalid input like `"not-an-email"` is happily accepted and
  stored. The AI Dev Agent demo task will add the missing validation.
