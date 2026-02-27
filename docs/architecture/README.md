# Architecture Documentation

This directory contains architecture-related documentation for PetWise.

## Folder Structure

### /c4
C4 architectural diagrams (PlantUML):
- `c4-context.puml` — C1: System Context
- `c4-container.puml` — C2: Containers
- `c4-components.puml` — C3: Components

### /domain
Domain knowledge:
- `business-rules.md` — Domain invariants and constraints
- `glossary.md` — Domain vocabulary and definitions
- `erd.puml` — Entity-Relationship Diagram

### /sequences
Behavioral diagrams (PlantUML) for each use case:
- `uc01-create-tutor.puml` through `uc06-edit-delete-records.puml`

## Rendering Diagrams

```bash
make diagrams
```
