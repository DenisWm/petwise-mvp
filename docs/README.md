# PetWise Documentation

This directory contains the canonical documentation and Jekyll site for the PetWise project.

## Project overview

PetWise is an MVP application that manages tutors, pets, and appointments for a pet daycare and hotel service. The documentation includes:

- Getting started and developer onboarding
- API reference and OpenAPI specification
- Use cases with portable spec files
- Architecture (C4) diagrams, domain model, and ADRs
- Build system and contribution guidance

Refer to the repository root `README.md` for a high-level project summary.

## Local preview

The documentation site is Jekyll-based and can be run locally using Docker Compose.

### Prerequisites
- Docker and Docker Compose

### Run the site locally

```bash
cd docs
docker-compose up
# The site will be available at http://localhost:4000
```

## Documentation structure

```
docs/
├─ _config.yml
├─ index.md
├─ getting-started.md
├─ architecture.md
├─ use-cases.md
├─ build-system.md
├─ api-reference.md
├─ contributing.md
├─ faq.md
├─ architecture/
│  ├─ overview.md
│  ├─ decisions.md
│  ├─ domain.md
│  ├─ diagrams.md
│  ├─ c4/           (PlantUML sources)
│  ├─ domain/       (business-rules, glossary, ERD)
│  └─ sequences/    (PlantUML sources)
├─ use-cases/
│  ├─ uc-01-register-tutor.md      (Jekyll pages)
│  ├─ ...
│  └─ specs/                        (portable specs)
│     ├─ uc-template.md
│     ├─ uc-01-register-tutor.md
│     └─ ...
├─ api/
│  ├─ guidelines.md
│  └─ openapi.yaml
└─ assets/
```

## Rendering diagrams

```bash
make diagrams
```

## Maintenance notes

- Keep ADRs and the domain model up to date when architectural changes occur
- Update use case specs in `use-cases/specs/` (the authoritative source); Jekyll pages are thin wrappers
- Regenerate `.puml` diagrams with `make diagrams` when sources change
