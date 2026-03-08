# PetWise

> A pedagogical Spring Boot application demonstrating Clean Architecture, Domain-Driven Design (DDD), and modern Gradle build practices.

**PetWise** is an MVP system for managing pet daycare and hotel services — built as a learning resource and bootstrap template for Java projects.

---

## Architecture overview

Three-layer architecture inspired by Clean Architecture and Hexagonal Architecture:

```
┌─────────────────┐
│ infrastructure  │  ← REST controllers, JPA persistence, Spring Boot, Keycloak
├─────────────────┤
│  application    │  ← Use cases orchestrating business workflows
├─────────────────┤
│    domain       │  ← Business rules, aggregates, entities, value objects
└─────────────────┘
```

### Modules

| Module | Purpose | Dependencies |
|:-------|:--------|:-------------|
| `domain` | Aggregates (`Tutor`, `Appointment`), entities (`Pet`), value objects, validation | None (pure Java) |
| `application` | Use-case classes for business workflows | `domain` |
| `infrastructure` | Spring Boot web layer, JPA repositories, security configuration | `application`, `domain` |
| `build-logic` | Gradle convention plugins (Java, Spring Boot, Spotless, JaCoCo, OWASP) | N/A (composite build) |

---

## Getting started

### Prerequisites

- Java 21 (JDK)
- Docker (for PostgreSQL, Keycloak, and PlantUML diagrams)

### Run locally

```bash
# Build the project
./gradlew build

# Start everything (PostgreSQL + Keycloak + application)
docker compose up --build

# Or start only infrastructure, then run with Gradle
docker compose up -d db keycloak
./gradlew :infrastructure:bootRun

# Access the API (requires a JWT — see docs/api-reference)
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/tutors
```

---

## Documentation

Full documentation lives under `docs/` and is published as a Jekyll site.

Key sections:

| Page | Description |
|:-----|:------------|
| [Getting Started](docs/getting-started.md) | Setup, build, and run |
| [Architecture](docs/architecture/) | C4 diagrams, domain model, ADRs, sequence diagrams |
| [Use Cases](docs/use-cases/) | Functional specs (UC-01 through UC-06) |
| [API Reference](docs/api-reference.md) | REST endpoints, authentication, error format |
| [Build System](docs/build-system.md) | Gradle convention plugins and version catalog |
| [Operations](docs/operations.md) | Logging, actuator, runtime debugging |
| [Contributing](docs/contributing.md) | Workflow, coding standards, PR checklist |

---

## Testing

```bash
./gradlew test                    # Run all tests
./gradlew test jacocoTestReport   # Run tests + generate coverage report
```

Coverage reports: `<module>/build/reports/jacoco/test/html/index.html`

---

## Diagrams

PlantUML sources (`.puml`) under `docs/` are rendered with Make + Docker:

```bash
make diagrams          # Render all .puml → .png
make diagrams publish  # Copy PNGs into docs/assets/diagrams/
make clean             # Remove generated PNGs
```

---

## Contributing

See [docs/contributing.md](docs/contributing.md). Quick checklist:

1. Follow project style (`./gradlew spotlessApply`)
2. Update documentation when adding features
3. Add ADRs for architectural changes
4. Ensure tests pass and coverage holds
5. Run `make diagrams` if `.puml` sources change

---

## License

This project is provided for educational purposes and as a template. Use as needed.
