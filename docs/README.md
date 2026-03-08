# PetWise Documentation

This directory contains the project documentation and Jekyll site.

## Local preview

```bash
cd docs
docker compose up
# Site available at http://localhost:4000
```

## Structure

```
docs/
├─ _config.yml
├─ index.md                        Home
├─ getting-started.md              Setup and build
├─ architecture.md                 Architecture (parent)
│  ├─ overview.md                  Layers, modules, patterns
│  ├─ decisions.md                 ADRs
│  ├─ domain.md                    Entity model + ERD
│  │  └─ business-rules.md         Rules, glossary, DDD mapping
│  └─ diagrams.md                  C4 + sequence diagram index
├─ use-cases.md                    Use cases (parent)
│  ├─ uc-01 … uc-06.md            Self-contained use case pages
├─ build-system.md                 Gradle convention plugins
├─ api-reference.md                Auth, error format, Swagger/Redoc links
├─ operations.md                   Logging, actuator, debugging
├─ contributing.md                 Contribution guide
└─ api/
   ├─ guidelines.md                REST conventions
   ├─ openapi.yaml                 Auto-generated spec
   └─ redoc.html                   Interactive API viewer
```

## Maintenance

- Use case pages are self-contained — no external spec files.
- Business rules and glossary live in a single file: `architecture/domain/business-rules.md`.
- OpenAPI spec is auto-generated: `./gradlew :infrastructure:generateOpenApiDocs`.
- ERD is auto-generated from the live DB: `make erd` (requires `make infra-up`).
- Diagrams: `make diagrams publish`.
