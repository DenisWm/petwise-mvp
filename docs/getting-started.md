---
layout: default
title: Getting Started
nav_order: 2
description: "Set up, build, and run PetWise locally"
---

# Getting Started
{: .no_toc }

This guide will help you set up, run, and understand the PetWise project.
{: .fs-6 .fw-300 }

## Table of contents
{: .no_toc .text-delta }

1. TOC
{:toc}

---

## Prerequisites

| Tool | Required? | Notes |
|:-----|:----------|:------|
| **Java 21** (JDK) | Yes | [Download](https://adoptium.net/) |
| **Docker** | Recommended | Runs PostgreSQL, Keycloak, and PlantUML |
| **Make** | Optional | Diagram rendering shortcuts |

### Verify Installation

```bash
java -version     # Should show Java 21
docker --version  # 20.10+
```

---

## Clone and Build

### 1. Clone the Repository

```bash
git clone https://github.com/deniswm/petwise-mvp.git
cd petwise
```

### 2. Build the Project

```bash
# On Unix/Linux/macOS
./gradlew build

# On Windows
gradlew.bat build
```

This will:
- Compile all modules (domain, application, infrastructure)
- Run tests
- Apply code formatting (Spotless)
- Generate code coverage reports

---

## Running the Application

### Docker Compose (recommended)

```bash
docker compose up --build
```

This starts:
- **PostgreSQL** on port 5432
- **Keycloak** on port 9080
- **PetWise API** on port 8080

### Standalone Docker

```bash
docker build -t petwise:latest .
docker run -p 8080:8080 petwise:latest
```

### Local Development

```bash
./gradlew :infrastructure:bootRun
```

{: .note }
> Running locally requires PostgreSQL and Keycloak. Use `docker compose up -d db keycloak` to start them, then run the application with Gradle.

---

## Exploring the API

{: .note }
> All endpoints require a Bearer JWT from Keycloak. See the [API Reference — Authentication](api-reference#authentication) section for how to obtain a token. The examples below assume a `$TOKEN` variable is set.

### Create a Tutor

```bash
curl -X POST http://localhost:8080/api/tutors \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Alice Smith",
    "email": "alice@example.com",
    "phone": "+1234567890"
  }'
```

### List Tutors

```bash
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/tutors
```

{: .highlight }
For the complete API reference, see the [API Reference](api-reference) page.

---

## Project Structure

```
petwise/
├── domain/              # Business logic (pure Java)
├── application/         # Use cases (orchestration)
├── infrastructure/      # Spring Boot, REST, JPA
├── build-logic/         # Gradle convention plugins
├── docs/               # Documentation
└── gradle/             # Gradle wrapper
```

### Module Dependencies

```
infrastructure → application → domain
```

Each layer depends only on inner layers, following Clean Architecture principles.

---

## Running Tests

```bash
# Run all tests
./gradlew test

# Run tests for specific module
./gradlew :domain:test

# Run with coverage report
./gradlew test jacocoTestReport
```

Coverage reports are in: `<module>/build/reports/jacoco/test/html/index.html`

---

## Code Quality

### Format Code

```bash
# Check formatting
./gradlew spotlessCheck

# Apply formatting
./gradlew spotlessApply
```

PetWise uses **Spotless** with **Google Java Format** to ensure consistent code style.

---

## Next Steps

1. [**Architecture Overview**](architecture/overview) — Understand the design
2. [**Domain Model**](architecture/domain/) — Review entities and business rules
3. [**API Reference**](api-reference) — Explore the REST API
4. [**Operations Guide**](operations) — Logging, actuator, runtime debugging
5. [**Contributing Guide**](contributing) — Learn how to contribute

---

## Ports Reference

| Service | Port | Notes |
|:--------|:-----|:------|
| PetWise API | 8080 | REST endpoints (context-path `/api`) |
| Keycloak | 9080 | Admin console + token endpoints |
| PostgreSQL | 5432 | Shared by app and Keycloak (separate databases) |
| Actuator | 9090 | Management endpoints (`/management/health`, `/management/loggers`) |

---

## Troubleshooting

### Port 8080 Already in Use

If port 8080 is already in use:

```bash
# Find and kill the process (macOS/Linux)
lsof -ti:8080 | xargs kill -9

# Windows
netstat -ano | findstr :8080
```

Or change the port in `infrastructure/src/main/resources/application.yml`

### Gradle Build Fails

```bash
# Clean and rebuild
./gradlew clean build
```

### Docker Issues

```bash
# Reset Docker Compose
docker compose down -v
docker compose up --build
```

---

{: .note-title }
> Need Help?
>
> - [Report an issue](https://github.com/deniswm/petwise-mvp/issues)
> - [Start a discussion](https://github.com/deniswm/petwise-mvp/discussions)

