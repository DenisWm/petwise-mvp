# Architecture Documentation

Source files for architecture diagrams and domain knowledge.

## /c4
C4 architectural diagrams (PlantUML):
- `c4-context.puml` — C1: System Context
- `c4-container.puml` — C2: Containers
- `c4-components.puml` — C3: Components

## /domain
- `business-rules.md` — Domain invariants, glossary, and DDD mapping
- `erd.puml` — Entity-Relationship Diagram

## /sequences
Behavioral diagrams (PlantUML) for each use case:
- `uc01-create-tutor.puml` through `uc06-edit-delete-records.puml`

## Rendering

```bash
make diagrams   # Render .puml → .png
make erd        # Generate live ERD from database (requires make infra-up)
```
