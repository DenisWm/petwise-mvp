---
layout: default
title: Architecture
nav_order: 3
has_children: true
---

# Architecture

PetWise follows **Clean Architecture** with three layers: domain (business rules), application (use case orchestration), and infrastructure (Spring Boot, REST, JPA).

This section covers:

- [Overview](architecture/overview) — Layers, modules, dependency rules, and testing strategy
- [Decision Records](architecture/decisions) — ADRs documenting key design choices
- [Domain Model](architecture/domain) — Entities, aggregates, value objects, and business rules
- [Diagrams](architecture/diagrams) — C4 model, sequence diagrams, and ERD
