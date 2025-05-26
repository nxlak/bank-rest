# bank-rest

## Docker Setup

### 1. Build the Docker image

From the project root, run:

```bash
docker build -t bank-rest-app:latest .
```

### 2. Run with Docker Compose

The provided `docker-compose.yml` will spin up both the application and PostgreSQL:

```bash
docker compose up --build
```

Once started, the API will be available at:

```
http://localhost:8080
```
