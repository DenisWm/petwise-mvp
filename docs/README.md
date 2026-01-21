# PetWise Documentation

This directory contains the canonical documentation and Jekyll site for the PetWise project.

This README covers:
- Contents and structure of the documentation
- How to run and preview the Jekyll site locally
- Guidelines for maintaining documentation and diagrams

## Project overview

PetWise is an MVP application that manages tutors, pets, and appointments. The documentation includes:

- Getting started and developer onboarding guides
- API reference and OpenAPI specification
- Use cases and sequence diagrams
- Architecture (C4) diagrams, domain model, and ADRs
- Contribution and build system guidance

Refer to the repository root `README.md` for a high-level project summary.

## Local preview

The documentation site is Jekyll-based and can be run locally using Docker Compose.

### Prerequisites
- Docker and Docker Compose

### Run the site locally

```bash
# From the repository root
docker-compose up
# The site will be available at http://localhost:4000
```

## Documentation structure

Top-level files and folders include:

```
docs/
├─ _config.yml
├─ index.md
├─ getting-started.md
├─ architecture-deep-dive.md
├─ contributing.md
├─ build-system.md
├─ implementation-roadmap.md
├─ github-project.md
├─ api-reference.md
└─ assets/
```

## Rendering diagrams

Diagram sources are PlantUML (`.puml`) files under `docs/`. Use the repository Makefile to render and publish PNGs:

```bash
make diagrams
```

## Adding content

- Create pages with standard Jekyll front matter (`layout`, `title`, `nav_order`)
- Use collections for grouped content (`_architecture`, `_use-cases`, `_api`)

## Validation and testing

To validate the generated site and check for broken links, use HTML validation tools against `_site` after building.

## Maintenance notes

When updating documentation:
- Update or add `.puml` diagram sources as needed
- Keep ADRs and the domain model up to date when architectural changes occur
