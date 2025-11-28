---
layout: default
title: Diagrams
parent: Architecture
nav_order: 4
---

# Architecture Diagrams
{: .no_toc }

Visual representations of the system architecture using C4 model and sequence diagrams.
{: .fs-6 .fw-300 }

## Table of contents
{: .no_toc .text-delta }

1. TOC
{:toc}

---

## C4 Model Diagrams

The architecture is documented using the **C4 model** for visualizing software architecture at different levels of abstraction.

### Level 1: System Context

**Shows:** How PetWise fits in the overall context.

**Key Elements:**
- End users (Attendants)
- PetWise system
- External systems (future integrations)

📄 [View PlantUML Source](https://github.com/deniswm/petwise-mvp/blob/master/docs/architecture/c4/c4-context.puml)

---

### Level 2: Container Diagram

**Shows:** High-level technology choices and communication patterns.

**Containers:**
- **Web Browser** - User interface (future)
- **REST API** - Spring Boot application
- **Database** - SQLite (MVP) / PostgreSQL (production)

📄 [View PlantUML Source](https://github.com/deniswm/petwise-mvp/blob/master/docs/architecture/c4/c4-container.puml)

---

### Level 3: Component Diagram

**Shows:** Internal structure of the REST API.

**Components:**
- **Application Module** - Use cases and ports
- **Domain Module** - Entities, value objects, business logic
- **Infrastructure Module** - REST controllers, JPA repositories, database

**Key Patterns:**
- Dependency inversion (domain depends on nothing)
- Port-adapter architecture
- Vertical slices per feature

📄 [View PlantUML Source](https://github.com/deniswm/petwise-mvp/blob/master/docs/architecture/c4/c4-components.puml)

---

## Sequence Diagrams

Each use case has a detailed sequence diagram showing the flow through layers.

### UC-01: Register Tutor

**Flow:**
1. Client → REST Controller
2. Controller → Use Case
3. Use Case → Domain Entity
4. Use Case → Gateway (repository)
5. Gateway → JPA Repository
6. Response flows back

📄 [View PlantUML Source](https://github.com/deniswm/petwise-mvp/blob/master/docs/architecture/sequences/uc01-create-tutor.puml)

---

### UC-02: Register Pet

**Flow:**
1. Client → REST Controller
2. Controller → Use Case
3. Use Case validates tutor exists (via TutorGateway)
4. Use Case creates Pet entity
5. Use Case persists via PetGateway
6. Response flows back

📄 [View PlantUML Source](https://github.com/deniswm/petwise-mvp/blob/master/docs/architecture/sequences/uc02-create-pet.puml)

---

### UC-03: Create Appointment

**Flow:**
1. Client → REST Controller
2. Controller → Use Case
3. Use Case validates pet exists (via PetGateway)
4. Use Case creates Appointment entity
5. Use Case persists via AppointmentGateway
6. Response flows back

📄 [View PlantUML Source](https://github.com/deniswm/petwise-mvp/blob/master/docs/architecture/sequences/uc03-create-appointment.puml)

---

### UC-04: Change Appointment Status

**Flow:**
1. Client → REST Controller
2. Controller → Use Case
3. Use Case fetches appointment (via AppointmentGateway)
4. Use Case calls `appointment.changeStatus(newStatus)`
5. Domain validates transition rules
6. Use Case persists updated appointment
7. Response flows back

**Key Point:** Status transition logic is in the domain entity, not the use case.

📄 [View PlantUML Source](https://github.com/deniswm/petwise-mvp/blob/master/docs/architecture/sequences/uc04-change-appointment-status.puml)

---

### UC-05: View Daily Agenda

**Flow:**
1. Client → REST Controller
2. Controller → Use Case
3. Use Case queries appointments by date range (via AppointmentGateway)
4. Use Case optionally enriches with pet/tutor data
5. Response flows back

📄 [View PlantUML Source](https://github.com/deniswm/petwise-mvp/blob/master/docs/architecture/sequences/uc05-view-daily-agenda.puml)

---

### UC-06: Edit/Delete Records

**Flow:**
- **Update:** Fetch → Modify → Validate → Persist
- **Delete:** Validate no dependencies → Delete

📄 [View PlantUML Source](https://github.com/deniswm/petwise-mvp/blob/master/docs/architecture/sequences/uc06-edit-delete-records.puml)

---

## Domain Model Diagram

**Entity Relationship Diagram (ERD)** showing:
- Entities: Tutor, Pet, Appointment
- Relationships: Tutor → Pet → Appointment
- Attributes and types

📄 [View PlantUML Source](https://github.com/deniswm/petwise-mvp/blob/master/docs/architecture/domain/erd.puml)

---

## How to View Diagrams

### Option 1: GitHub (Automatic Rendering)

GitHub renders PlantUML diagrams automatically when viewing `.puml` files.

### Option 2: VS Code

Install **PlantUML extension**:
```
code --install-extension jebbs.plantuml
```

Then open any `.puml` file and press `Alt+D` to preview.

### Option 3: Online Viewer

Copy diagram source and paste into:
- [PlantUML Online](http://www.plantuml.com/plantuml/uml/)
- [PlantText](https://www.planttext.com/)

### Option 4: Local PlantUML

Install PlantUML and Graphviz, then:
```bash
plantuml docs/architecture/**/*.puml
```

---

## Diagram Sources

All diagram source files are in the repository:

- 📁 [C4 Diagrams](https://github.com/deniswm/petwise-mvp/tree/master/docs/architecture/c4)
- 📁 [Sequence Diagrams](https://github.com/deniswm/petwise-mvp/tree/master/docs/architecture/sequences)
- 📁 [Domain Diagrams](https://github.com/deniswm/petwise-mvp/tree/master/docs/architecture/domain)

---

## Related

- [Architecture Overview](overview) - Start here for high-level view
- [Domain Model](domain) - Detailed entity documentation
- [Use Cases](../use-cases) - Functional requirements

