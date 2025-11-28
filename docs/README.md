# PetWise – Documentation

This folder contains all documentation for the PetWise MVP, covering tutors, pets, appointments, and the architectural approach behind the system.

The documentation is organized to help contributors and maintainers understand what the system does, how it works, and why it was designed this way.

---

## 🚀 Quick Start

**New to PetWise?** Start here:

1. **[Getting Started Guide](GETTING_STARTED.md)** – Set up, build, and run the project
2. **[Architecture Deep Dive](ARCHITECTURE_DEEP_DIVE.md)** – Understand the design patterns and technical decisions
3. **[Project README](../README.md)** – Overview, objectives, and technology stack

---

## 📘 How to Navigate This Documentation

### For Developers New to the Project

1. **[Getting Started](GETTING_STARTED.md)**  
   Installation, running locally, first API calls

2. **[Use Cases](use-cases/)**  
   Understand system behavior and user interactions (start with UC-01)

3. **[Architecture](architecture/)**  
   Explore C4 diagrams, domain model (ERD, glossary), and ADRs

4. **[Architecture Deep Dive](ARCHITECTURE_DEEP_DIVE.md)**  
   Deep technical exploration of Clean Architecture, DDD, and design patterns

### For API Consumers

1. **[API Quick Reference](API_REFERENCE.md)** – Quick guide to all endpoints with examples
2. **[API Guidelines](api/guidelines.md)** – REST conventions, error handling, pagination
3. **[OpenAPI Specification](api/openapi.yaml)** – Complete API reference

### For Contributors

1. **[Contributing Guide](CONTRIBUTING.md)** – How to contribute code, tests, and documentation
2. **[Build System Guide](BUILD_SYSTEM.md)** – Understanding Gradle, convention plugins, and build configuration
3. **[Implementation Roadmap](IMPLEMENTATION_ROADMAP.md)** – 34 issues broken down into actionable tasks
4. **[GitHub Project Setup](GITHUB_PROJECT_SETUP.md)** – Configure GitHub Projects, labels, and workflows

### For Architects and Maintainers

Frequently referenced materials:
- **[ADRs](architecture/decisions/)** – Architectural decision records
- **[Domain Rules](architecture/domain/business-rules.md)** – Business invariants and constraints
- **[ERD](architecture/domain/erd.puml)** – Entity-Relationship Diagram (canonical schema)
- **[Sequence Diagrams](architecture/sequences/)** – Runtime interactions for each use case

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
