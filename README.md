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
| GET    | /                | API information   |
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

## CORS Configuration

The API includes environment-based CORS configuration to handle cross-origin requests from frontend applications.

### Environment Variables

Add these CORS-related variables to your `.env` file:

```bash
# CORS Configuration
CORS_ALLOWED_ORIGINS=https://your-frontend-domain.com,http://localhost:3000
CORS_ALLOWED_METHODS=GET,POST,PUT,DELETE,OPTIONS
CORS_ALLOWED_HEADERS=*
CORS_ALLOW_CREDENTIALS=true
CORS_MAX_AGE=3600
```

### Configuration Details

- **CORS_ALLOWED_ORIGINS**: Comma-separated list of allowed origins (frontend URLs)
- **CORS_ALLOWED_METHODS**: HTTP methods allowed for CORS requests
- **CORS_ALLOWED_HEADERS**: Headers allowed in CORS requests (* allows all)
- **CORS_ALLOW_CREDENTIALS**: Whether to allow credentials in CORS requests
- **CORS_MAX_AGE**: How long browsers can cache CORS preflight responses (seconds)

### Production Example

For production deployment with a specific frontend domain:

```bash
CORS_ALLOWED_ORIGINS=https://nuxt-contact-m48o97.apps.shotlin.in
CORS_ALLOWED_METHODS=GET,POST,PUT,DELETE,OPTIONS
CORS_ALLOWED_HEADERS=Content-Type,Authorization,X-Requested-With
CORS_ALLOW_CREDENTIALS=true
CORS_MAX_AGE=3600
```

---

## Example Requests

### Root endpoint (API info)
```bash
curl http://localhost:5005/
```

Response:
```json
{
  "message": "Welcome to Java Contact API",
  "app": "java-contact-api",
  "version": "1.0.0",
  "environment": "development",
  "language": "Java",
  "framework": "Spring Boot 3.2",
  "timestamp": "2026-05-15T21:21:34",
  "endpoints": {
    "health": "GET /health - Health check",
    "contacts": "GET /contacts - Get all contacts",
    "create_contact": "POST /contacts - Create a new contact",
    "get_contact": "GET /contacts/{id} - Get contact by ID"
  },
  "documentation": {
    "description": "Simple Spring Boot Contact API for managing contacts",
    "database": "MySQL with JPA",
    "cache": "Redis",
    "cors_enabled": true
  }
}
```

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
