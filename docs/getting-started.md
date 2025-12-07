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

Before you begin, ensure you have the following installed:

- **Java 21** (JDK) - [Download](https://adoptium.net/)
- **Docker** (optional) - For PostgreSQL and diagram rendering
- **Make** (optional) - For diagram generation

### Verify Installation

```bash
java -version    # Should show Java 21
git --version
docker --version  # Optional
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

### Option 1: Local Development (SQLite)

By default, PetWise uses **SQLite** for local development:

```bash
./gradlew :infrastructure:bootRun
```

The application starts on **http://localhost:8080**

{: .note }
> SQLite requires zero configuration and is perfect for local development and learning.

### Option 2: Docker Compose (PostgreSQL)

To run with PostgreSQL in Docker:

```bash
docker-compose up --build
```

This starts:
- **PostgreSQL** on port 5432
- **PetWise API** on port 8080

### Option 3: Standalone Docker

```bash
docker build -t petwise:latest .
docker run -p 8080:8080 petwise:latest
```

---

## Exploring the API

### Health Check

```bash
curl http://localhost:8080/actuator/health
```

### Example: Create a Tutor

```bash
curl -X POST http://localhost:8080/api/v1/tutors \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Alice Smith",
    "email": "alice@example.com",
    "phone": "+1234567890"
  }'
```

**Response:**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Alice Smith",
  "email": "alice@example.com",
  "phone": "+1234567890"
}
```

### List Tutors

```bash
curl http://localhost:8080/api/v1/tutors
```

{: .highlight }
For a complete API reference, see the [API Reference](api-reference) page.

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

Now that you have the project running:

1. **Explore the Architecture** - Read [Architecture Deep Dive](architecture-deep-dive)
2. **Understand the Domain** - Review [Domain Model](architecture/domain/)
3. **Try the API** - See [API Reference](api-reference)
4. **Contribute** - Check out the [Contributing Guide](contributing)

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
docker-compose down -v
docker-compose up --build
```

---

{: .note-title }
> Need Help?
>
> - Check the [FAQ](faq)
> - [Report an issue](https://github.com/deniswm/petwise-mvp/issues)
> - [Start a discussion](https://github.com/deniswm/petwise-mvp/discussions)

