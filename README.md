# PetWise

> A pedagogical Spring Boot application demonstrating Clean Architecture, Domain-Driven Design (DDD), and modern Gradle build practices.

**PetWise** is an MVP system for managing pet daycare and hotel services. It is presented as a learning resource and a bootstrap template for Java projects.

---

## Project objectives

This project serves three purposes:

1. Learning resource – Demonstrate Clean Architecture, DDD tactical patterns (Aggregates, Entities, Value Objects), and use-case-driven development.
2. Reference implementation – Example of a multi-module Gradle project with convention plugins, linting, and testing.
3. Bootstrap template – Base for new Spring Boot projects.

---

## Architecture overview

PetWise follows a three-layer architecture inspired by Clean Architecture and Hexagonal Architecture:

```
┌─────────────────┐
│ infrastructure  │  ← REST controllers, JPA persistence, Spring Boot configuration
├─────────────────┤
│  application    │  ← Use cases orchestrating business workflows
├─────────────────┤
│    domain       │  ← Business rules, aggregates, entities, value objects
└─────────────────┘
```

### Modules

- `domain` – Pure Java: aggregates (`Tutor`, `Appointment`), entities (`Pet`), domain events, validation, and DDD base classes.
- `application` – Use-case classes implementing business workflows; depends on `domain`.
- `infrastructure` – Spring Boot web layer, JPA repositories, configuration; depends on `application` and `domain`.
- `build-logic` – Gradle convention plugins for Java, Spring Boot, linting (Spotless), and coverage (JaCoCo).

---

## Getting started

### Prerequisites

- Java 21 (JDK)
- Docker (optional, for diagrams and PostgreSQL)
- Make (optional, for rendering diagrams)

### Run locally

```bash
# Build the project
./gradlew build

# Run with Docker Compose (PostgreSQL + application)
docker-compose up --build

# Or run the application with Gradle (requires PostgreSQL)
# Start only the database: docker-compose up db
./gradlew :infrastructure:bootRun

# Access the API
curl http://localhost:8080/api/v1/tutors
```


---

## Documentation

Documentation and Jekyll site is maintained under `docs/`.

Key sections:

- `docs/README.md` – navigation guide for documentation
- `docs/architecture/` – C4 diagrams, domain model, ADRs, sequence diagrams
- `docs/use-cases/` – functional specifications (UC-01 to UC-06)
- `docs/api/` – OpenAPI specification and REST guidelines
- `docs/diagrams/` – diagram sources

Refer to `docs/` for the published site content.

---

## Testing

```bash
# Run all tests
./gradlew test

# Run tests with coverage report
./gradlew test jacocoTestReport
```

Coverage reports are located under `build/reports/jacoco/test/html/index.html`.

---

## Build system

PetWise uses Gradle 8.14+ with Kotlin DSL and convention plugins defined in `build-logic/`.

Plugins include:

- `petwise.java-library-conventions` – Java library configuration (JUnit, Spotless, JaCoCo)
- `petwise.spring-boot-app-conventions` – Spring Boot application configuration
- `petwise.lint-conventions` – Code formatting with Spotless
- `petwise.jacoco-conventions` – Coverage reporting

This design centralizes build configuration and reduces duplication across modules.

---

## Diagrams

PlantUML sources (`.puml`) under `docs/` can be rendered with the provided Makefile targets.

```bash
# Render all diagrams
make diagrams

# Render a specific diagram
make docs/architecture/c4/c4-context.png

# Clean generated PNGs
make clean
```

---

## Contributing

Contributions are welcome. See `docs/contributing.md` for contribution guidelines.

Quick contributor checklist:

1. Follow the project style and formatting (Spotless).
2. Update documentation when adding features.
3. Add ADRs for architectural changes when appropriate.
4. Ensure tests pass and coverage remains sufficient.
5. Run `make diagrams` if `.puml` sources are modified.

---

## License

This project is provided for educational purposes and as a template. Use as needed.
