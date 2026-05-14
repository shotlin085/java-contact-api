# java-contact-api

Simple Spring Boot Contact API — Java enterprise-style backend demo.

## Stack
- **Language:** Java 17
- **Framework:** Spring Boot 3.2
- **Database:** MySQL (Spring Data JPA)
- **Cache:** Redis
- **Build:** Maven

## Routes

| Method | Path             | Description       |
|--------|------------------|-------------------|
| GET    | /health          | Health check      |
| POST   | /contacts        | Create a contact  |
| GET    | /contacts        | Get all contacts  |
| GET    | /contacts/{id}   | Get one contact   |

---

## Run with Docker

### Step 1 — Start MySQL (external container)

```bash
docker run -d \
  --name mysql-contact \
  -e MYSQL_ROOT_PASSWORD=password \
  -e MYSQL_DATABASE=java_contact_demo \
  -p 3306:3306 \
  mysql:8.0
```

### Step 2 — Start Redis (external container)

```bash
docker run -d \
  --name redis-contact \
  -p 6379:6379 \
  redis:7-alpine
```

### Step 3 — Build the app image

```bash
docker build -t java-contact-api .
```

### Step 4 — Run the app

```bash
docker run -d \
  --name java-contact-api \
  -p 5005:5005 \
  -e SERVER_PORT=5005 \
  -e MYSQL_HOST=host.docker.internal \
  -e MYSQL_PORT=3306 \
  -e MYSQL_USER=root \
  -e MYSQL_PASSWORD=password \
  -e MYSQL_DATABASE=java_contact_demo \
  -e REDIS_HOST=host.docker.internal \
  -e REDIS_PORT=6379 \
  -e APP_NAME=java-contact-api \
  -e APP_ENV=development \
  java-contact-api
```

> `host.docker.internal` lets the app container reach MySQL and Redis running on your host (or other containers mapped to host ports).  
> On Linux, add `--add-host=host.docker.internal:host-gateway` to the run command.

---

## Run locally (no Docker)

Make sure MySQL and Redis are running, then:

```bash
mvn spring-boot:run
```

---

## Example Requests

### Health check
```bash
curl http://localhost:5005/health
```

Response:
```json
{
  "status": "ok",
  "app": "java-contact-api",
  "language": "Java",
  "framework": "Spring Boot",
  "database": { "type": "MySQL", "connected": true },
  "cache": { "type": "Redis", "connected": true }
}
```

### Create contact
```bash
curl -X POST http://localhost:5005/contacts \
  -H "Content-Type: application/json" \
  -d '{"name": "Sayan Mondal", "email": "sayan@example.com", "phone": "1234567890"}'
```

### Get all contacts
```bash
curl http://localhost:5005/contacts
```

### Get one contact
```bash
curl http://localhost:5005/contacts/1
```
