---
layout: default
title: Getting Started
nav_order: 2
description: "How to build, run, and test PetWise locally and with Docker."
---

# Getting Started — Quick Start

This page provides a minimal, tested path to run PetWise locally for development and a production-like setup using Docker Compose.

## Prerequisites

- Java 21 (JDK) with JAVA_HOME set
- Docker & Docker Compose (for production-like run)
- Git

## Quick local run (SQLite)

Windows (cmd.exe):

```
gradlew.bat build
gradlew.bat :infrastructure:bootRun
```

Unix/macOS:

```bash
./gradlew build
./gradlew :infrastructure:bootRun
```

The application listens on http://localhost:8080. Health: GET /actuator/health

## Docker Compose (PostgreSQL — production-like)

1. Build and start services:

   ```bash
   docker-compose up --build -d
   ```

2. Monitor health and logs:

   ```bash
   docker-compose logs -f
   ```

3. Stop and remove services:

   ```bash
   docker-compose down -v
   ```

## Testing the API

- Health: `curl http://localhost:8080/actuator/health`
- API docs (local): http://localhost:8080/swagger-ui.html (if enabled) or view `docs/api/openapi.yaml` for the canonical contract.

## Common tasks

- Run tests: `gradlew.bat test` (Windows) or `./gradlew test`
- Check formatting: `gradlew.bat spotlessCheck`
- Apply formatting: `gradlew.bat spotlessApply`

## Troubleshooting

- If port 8080 is occupied, change `server.port` in `infrastructure/src/main/resources/application.yml` or stop the interfering process.
- If the Gradle build fails, run a clean: `gradlew.bat clean build`
- If Docker Compose fails, run: `docker-compose down -v && docker-compose up --build`

## Further reading

- Architecture — design principles and deployment checklist
- API Reference — OpenAPI contract and examples
- Contributing — how to contribute and test changes
