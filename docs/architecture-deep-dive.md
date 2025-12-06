---
layout: default
title: Architecture Deep Dive
nav_order: 5
---

# Architecture — Deep Dive

PetWise is structured as a multi-module Java project that follows Clean Architecture principles and tactical DDD. It separates responsibilities into domain, application, and infrastructure layers to maximize testability and maintainability.

## Principles

- Dependency rule: inner layers (domain) have no dependencies on outer layers (infrastructure).
- Framework independence: domain model contains no framework types.
- Use-case driven: application behavior is expressed as discrete use-case classes for clarity and testability.
- Test-first mindset: fast unit tests for domain, focused use-case tests, and a small set of integration tests for infrastructure.

## Module boundaries

- `domain/`: Entities, aggregates, value objects, domain services, invariants.
- `application/`: Use-case orchestration, DTOs, and ports (interfaces) for persistence and external integration.
- `infrastructure/`: Spring Boot web controllers, JPA mapping, repository implementations, external adapters.

## Design patterns and examples

- Ports and adapters (hexagonal): Domain defines interfaces (ports) implemented by infrastructure adapters.
- Use-case classes: Each application use case is an explicit class that coordinates domain operations and persistence through ports.
- Gateways: Interfaces such as `TutorGateway` encapsulate persistence operations and mapping responsibilities.

## Trade-offs

- SQLite is adopted for local development to reduce setup friction; PostgreSQL is recommended for production (available via Docker Compose).
- The appointment lifecycle is intentionally forward-only (PENDING → ACTIVE → COMPLETED) to simplify invariants and reduce complexity when enforcing business rules.

## Deployment considerations

A production deployment must include:

- Secrets management (do not store credentials in the repository)
- Structured logging and correlation IDs
- Metrics and tracing (Prometheus, OpenTelemetry)
- Database backups and maintenance
- CI/CD with automated tests, linting, and artifact signing
- Operational runbooks (maintenance, incident response)

## Further reading

- Architecture overview and C4 diagrams in `docs/architecture/`
- ADRs in `docs/architecture/decisions/` for rationale behind key decisions
- API contract: `docs/api/openapi.yaml`
