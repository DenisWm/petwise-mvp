---
layout: default
title: Implementation Roadmap
nav_order: 8
---

# Implementation Roadmap
{: .no_toc }

Actionable GitHub issues organized into implementation phases to coordinate work via the project board.
{: .fs-6 .fw-300 }

## Project phases

Each phase groups related work by functional area. Phases are a planning aid; schedule and scope are maintained in the issue tracker.

### Phase 0 — Foundation
Focus: Project structure, build system, CI/CD

- Issue #1: Project setup and build configuration
- Issue #2: CI/CD pipeline with GitHub Actions
- Issue #3: Docker & Docker Compose setup

### Phase 1 — Domain & Core
Focus: Domain model, aggregates, validation

- Issue #4: Domain DDD base classes
- Issue #5: Domain validation framework
- Issue #6: Domain utilities
- Issue #7: Application use case base classes

### Phase 2 — Tutor Management
Focus: Tutor aggregate and related APIs

- Issue #8: Tutor domain model
- Issue #9: Tutor gateway interface
- Issue #10: Create Tutor use case
- Issue #11: Tutor JPA entity and repository
- Issue #12: Create Tutor REST endpoint
- Issue #13: List and Get Tutor endpoints
- Issue #14: Update and Delete Tutor endpoints

### Phase 3 — Pet Management
Focus: Pet entity and related APIs

- Issue #15: Pet domain model
- Issue #16: Pet gateway interface
- Issue #17: Create Pet use case and endpoint
- Issue #18: List and Get Pet endpoints
- Issue #19: Update and Delete Pet endpoints

### Phase 4 — Appointment Management
Focus: Appointment aggregate and lifecycle

- Issue #20: Appointment domain model
- Issue #21: Appointment gateway interface
- Issue #22: Create Appointment use case and endpoint
- Issue #23: Change Appointment status use case and endpoint
- Issue #24: Get and List Appointment endpoints

### Phase 5 — Queries & Reports
Focus: Read models, daily agenda, search

- Issue #25: View Daily Agenda use case and endpoint
- Issue #26: Search and filter enhancements

### Phase 6 — Polish & Documentation
Focus: Production readiness, testing, documentation

- Issue #27: Global exception handler
- Issue #28: API documentation with Swagger UI
- Issue #29: Database migration with Flyway
- Issue #30: Health checks and actuator endpoints
- Issue #31: Complete OpenAPI specification
- Issue #32: End-to-end integration tests
- Issue #33: Performance testing and optimizations
- Issue #34: Final documentation review

## Implementation strategy

Work is organized as vertical slices: each issue should, where applicable, deliver an end-to-end feature across layers (Controller → Use Case → Domain → Gateway → Repository). This approach aims to produce incremental, testable functionality and expose integration issues early.

## Checklist for issues

Suggested items to include in each issue description and PR:

- Domain logic implemented and covered by unit tests
- Use case implemented with tests
- REST endpoint implemented and wired to the use case (when applicable)
- Integration or end-to-end test(s) validating the feature
- OpenAPI/API docs updated (where relevant)
- Sequence diagram or architecture note updated if the design changed
- Code formatted and linted
- CI passes for the change

## Prioritization

Timing and detailed planning are managed per-issue in the project board. Use labels to record priority and other metadata.

Priority labels (recommended):
- P0-critical — Blocking other work
- P1-high — MVP core
- P2-medium — Important but not urgent
- P3-low — Enhancements and future work

## Example: Create Tutor use case

Issue: #10 — Create Tutor use case

Description (brief): Implement the Create Tutor use case and associated API surface.

Tasks (examples):
- Create `CreateTutorInput` and `CreateTutorOutput` DTOs
- Implement `CreateTutorUseCase` and domain interactions
- Persist via gateway/repository and expose a REST endpoint
- Add unit tests and an integration test
- Update API docs and sequence diagram where applicable

Acceptance criteria (example):
- Input validation enforced
- Domain invariants respected
- Persisted tutor retrievable via the API
- Tests validate the main scenarios

References:
- UC-01: Create Tutor — `use-cases/uc-01`
- Sequence diagram: `docs/architecture/sequences/uc01-create-tutor.puml`

## Next steps for contributors

- Review the project board and select a `Ready` item
- Create a descriptive branch, implement changes, and include tests and documentation updates
- Open a PR linking the issue, include testing instructions, and request review
- After merge, confirm issue state on the project board

## Further reading

- [GitHub Project](github-project) — Board guidance, labels, and automations
- [Contributing](contributing) — Development expectations and environment setup
- [Use Cases](use-cases) — Functional specifications and sequence diagrams
