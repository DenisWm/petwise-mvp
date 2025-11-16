# Architecture Documentation

This directory contains all architecture-related documentation for the PetWise MVP.  
It follows industry standards such as the C4 Model, lightweight DDD, and ADR (Architectural Decision Records).

The goal of this folder is to give developers, contributors, and future maintainers a clear and structured understanding of:

- how the system works
- how it is organized
- why certain decisions were made
- how domain rules operate

---

## Folder Structure

### /c4
Contains the C4 architectural diagrams representing the system from high-level to more detailed views:

- c4-context.puml – C1: System Context
- c4-container.puml – C2: Containers
- c4-components.puml – C3: Components

These diagrams help developers understand where the system fits, what modules exist, and how they communicate.

---

### /domain
Contains domain knowledge: the core business understanding behind PetWise.

- erd.puml – Entity-Relationship Diagram for Tutors, Pets, and Appointments
- business-rules.md – Domain invariants and constraints
- glossary.md – Domain vocabulary and definitions

The ERD is treated as the **canonical schema** for the MVP:

- It documents entities, relationships, and DDD roles (Aggregate Roots, Entities, enum-like value objects).
- It is the reference point when aligning database migrations, JPA mappings, and API representations.

This folder answers “What does the business do?” and “What rules govern the data?”

---

### /sequences
Behavioral diagrams (PlantUML) that illustrate how each use case works step-by-step.

Includes:

- uc01-create-tutor.puml
- uc02-create-pet.puml
- uc03-create-appointment.puml
- uc04-change-appointment-status.puml
- uc05-view-daily-agenda.puml
- uc06-edit-delete-records.puml

These diagrams show runtime interactions between:

- Web UI
- Controllers (infrastructure)
- UseCases (application layer)
- Gateways/Repositories (ports and adapters)
- Domain layer (aggregates, validators)

---

### /decisions
Contains Architectural Decision Records (ADRs) documenting why specific technical choices were made.

Current ADRs (MVP):

- adr-0001-database-sqlite-first.md – Why the MVP uses SQLite
- adr-0002-appointment-status-model.md – Why the forward-only status lifecycle was chosen
- adr-0003-domain-modeling-ddd-basics.md – Domain modeling with AggregateRoot, Entity, ValueObject
- adr-0004-repository-and-gateway-strategy.md – Repositories and gateways for persistence and isolation

ADRs preserve architectural reasoning and provide long-term clarity.

---

## How to Use This Folder

- New developers:  
  Start with c4-context.puml to understand the system’s scope, then read the domain glossary and ERD.

- Backend developers:  
  Use C3 components, business-rules.md, ERD, and sequence diagrams when implementing or changing UseCases, controllers, and repositories.

- Architects:  
  Use ADRs plus C4 diagrams to maintain consistency when making structural changes.

- Product and QA:  
  Refer to business-rules.md, ERD, and sequence diagrams for validation and acceptance criteria.

---

## Rendering Diagrams

All .puml diagrams under this folder can be rendered using Make + Docker from the project root.

Render all diagrams:

    make diagrams

Render a single diagram:

    make docs/architecture/c4/c4-context.png

Clean generated PNGs:

    make clean

Rendered .png files appear side-by-side next to their .puml sources.

---

## Contributing

When making architecture-impacting changes:

1. Update the relevant C4 diagrams.
2. Update the domain model or ERD if the data shape changes.
3. Update business-rules.md if domain rules change.
4. Add or update an ADR for decisions with architectural impact.
5. Keep sequence diagrams aligned with updated use case flows.

The architecture folder ensures the project remains scalable, understandable, and maintainable as it evolves.
