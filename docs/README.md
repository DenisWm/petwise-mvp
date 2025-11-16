# PetWise – Documentation

This folder contains all documentation for the PetWise MVP, covering tutors, pets, appointments, and the architectural approach behind the system.

The documentation is organized to help contributors and maintainers understand what the system does, how it works, and why it was designed this way.

---

## 📘 How to Navigate This Documentation

If you are new to the project, start with:

1. **Use Cases**  
   Understand system behavior and user interactions.

2. **Architecture**  
   Explore the system structure (C4 diagrams), domain model, and technical decisions (ADRs).

3. **API**  
   Review how external clients interact with the system through REST endpoints defined in the OpenAPI specification.

If you are modifying or extending the system, you may frequently refer to:

- Domain rules
- Sequence diagrams
- Business invariants
- Architectural Decision Records (ADRs)

---

## 📚 Table of Contents

### **1. Architecture**

Documentation describing the system from conceptual to component level.

- C4 Diagrams: architecture/c4
- Domain Model: architecture/domain
- Sequences: architecture/sequences
- Decisions (ADRs): architecture/decisions

---

### **2. API**

- OpenAPI Specification: api/openapi.yaml
- API Guidelines: api/guidelines.md

---

### **3. Use Cases**

- Use Case Definitions: use-cases/

---

### **4. Diagrams Overview**

- General UML Diagrams: diagrams/

---

## 🛠️ Rendering Diagrams

All `.puml` diagrams are rendered using **Make + Docker**.

From the project root:

### Render all diagrams

    make diagrams

### Render a single diagram

    make docs/architecture/c4/c4-context.png

### Clean generated PNGs

    make clean

Rendered `.png` files are created side-by-side with their corresponding `.puml` files.

---

## ✔ Contributing Notes

When modifying or adding documentation:

1. Update or add relevant `.puml` diagrams
2. Update business rules or domain sections as needed
3. Add a new ADR for architectural-impacting decisions
4. Run `make diagrams` to regenerate visuals
5. Ensure consistency across:
    - Use Cases
    - Domain Model
    - ERD
    - OpenAPI
    - Sequence Diagrams
    - ADRs

---
