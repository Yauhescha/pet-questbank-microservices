# QuestBank Microservices

A Java/Spring study project for storing interview questions and topics, with a separate authentication service, API gateway and server-rendered web interface.

## Services

| Directory | Responsibility | Default port |
| --- | --- | --- |
| `pet-questbank-front` | Thymeleaf UI and session-scoped tokens | 80 |
| `pet-questbank-gateway` | Routing and JWT request validation | 8080 |
| `pet-questbank-auth` | Registration, login, refresh and user data | 8081 |
| `pet-questbank` | Questions, topics and persistence | 8082 |

The stack uses **Java 21**, **Spring Boot 3.4.4**, Spring Security, Spring Cloud Gateway, JPA, H2 and Redis. Authentication schema migrations are under `pet-questbank-auth/src/main/resources/db/migration/`.

## Local development

Install JDK 21 and run Redis on `localhost:6379`. The module directories contain their own Gradle wrappers. Start each application in a separate terminal, with that module as the working directory:

| Module | macOS/Linux command |
| --- | --- |
| `pet-questbank-auth` | `./gradlew bootRun --args="--spring.data.redis.host=localhost"` |
| `pet-questbank` | `./gradlew bootRun` |
| `pet-questbank-gateway` | `./gradlew bootRun --args="--spring.profiles.active=local"` |
| `pet-questbank-front` | `./gradlew bootRun --args="--spring.profiles.active=local --server.port=8083"` |

On Windows, replace `./gradlew` with `gradlew.bat`. Open `http://localhost:8083` for this front-end configuration.

Run the question service with its default H2 configuration. Its `local` profile selects a MySQL driver and requires a complete matching database configuration. The auth service uses Flyway; the gateway and auth service must use the same JWT signing secret.

## Containers

`docker-compose.yml` describes Redis and the four services:

```sh
docker compose up --build
```

Before using the container configuration, review the Dockerfiles' build/runtime image tags and environment settings. In particular, the question service's default Redis host is `localhost`; inside Compose it should point to `redis-service`, for example through `SPRING_DATA_REDIS_HOST=redis-service`. Database files are copied into images, so persistence should be configured explicitly for a deployment.

## API and configuration

The gateway routes `/auth/**` and `/user/**` to authentication, and `/question/**` and `/topic/**` to QuestBank. Each module's `src/main/resources/application*` files distinguish local addresses from Compose service names.

This is a development project. Existing test classes mainly provide application-context checks; they do not establish end-to-end coverage of the authentication and question workflows.
