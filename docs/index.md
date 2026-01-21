---
layout: default
title: Home
nav_order: 1
description: "PetWise is a pedagogical Spring Boot application demonstrating Clean Architecture, Domain-Driven Design, and modern Java practices."
permalink: /
---

# PetWise Documentation
{: .fs-9 }

A pedagogical Spring Boot application demonstrating **Clean Architecture** and **Domain-Driven Design (DDD)**.
{: .fs-6 .fw-300 }

[Get Started](getting-started){: .btn .btn-primary .fs-5 .mb-4 .mb-md-0 .mr-2 }
[View on GitHub](https://github.com/deniswm/petwise-mvp){: .btn .fs-5 .mb-4 .mb-md-0 }

---

## What is PetWise?

PetWise is a **reference implementation** for building enterprise Java applications with:

- **Clean Architecture** - Dependency inversion, testability, framework independence
- **Domain-Driven Design** - Aggregates, entities, value objects, ubiquitous language
- **Modern Java Stack** - Java 21, Spring Boot 3, Gradle with convention plugins
- **Best Practices** - Multi-module project, vertical slices, comprehensive testing

{: .note }
> This project is designed for **learning**, **reference**, and as a **bootstrap template** for new Spring Boot projects.

---

## Quick Navigation

<div class="code-example" markdown="1">

### 🚀 Getting Started

New to the project? Start here:

1. [**Getting Started Guide**](getting-started) - Set up and run the project
2. [**Architecture Overview**](architecture/) - Understand the system structure
3. [**API Reference**](api-reference) - Explore the REST API

</div>

<div class="code-example" markdown="1">

### 📚 Core Documentation

**Architecture**
- [Architecture Deep Dive](architecture-deep-dive) - Design patterns and decisions
- [C4 Diagrams](architecture/c4/) - System visualization
- [Domain Model](architecture/domain/) - ERD, glossary, business rules
- [ADRs](architecture/decisions/) - Architectural decision records

**Implementation**
- [Use Cases](use-cases/) - UC-01 through UC-06
- [API Documentation](api/) - OpenAPI spec and guidelines
- [Build System](build-system) - Gradle convention plugins

</div>

<div class="code-example" markdown="1">

### 🤝 Contributing

Want to contribute or learn by implementing?

- [**Contributing Guide**](contributing) - How to contribute
- [**Implementation Roadmap**](implementation-roadmap) - 34 issues ready to implement
- [**GitHub Project**](github-project) - Team workflow

</div>

---

## Key Features

Clean Architecture
{: .label .label-blue }

Domain-Driven Design
{: .label .label-green }

Multi-Module Gradle
{: .label .label-purple }

Java 21
{: .label .label-yellow }

Spring Boot 3
{: .label .label-red }

---

## Technology Stack

| Layer | Technologies |
|:------|:-------------|
| **Backend** | Java 21, Spring Boot 3.5.7, Spring Data JPA |
| **Build** | Gradle 8.14 (Kotlin DSL), custom convention plugins |
| **Database** | SQLite (default), PostgreSQL (Docker) |
| **Testing** | JUnit 5, Spring Boot Test, JaCoCo |
| **Quality** | Spotless (Google Java Format) |
| **Diagrams** | PlantUML, C4 Model |
| **API** | REST, OpenAPI 3.0 |

---

## Learning Path

{: .highlight }
Follow this path to master the codebase:

**Level 1: Getting Started** (30-60 minutes)
1. Read the [Getting Started Guide](getting-started)
2. Run the project locally
3. Explore the API with curl

**Level 2: Domain Understanding** (1-2 hours)
1. Review [Use Cases](use-cases/)
2. Study the [Domain Model](architecture/domain/)
3. Understand [Business Rules](architecture/domain/business-rules)

**Level 3: Architecture** (2-3 hours)
1. Read [Architecture Deep Dive](architecture-deep-dive)
2. View [C4 Diagrams](architecture/c4/)
3. Trace a use case through all layers

**Level 4: Contributing** (1-2 hours)
1. Read [Contributing Guide](contributing)
2. Review [Build System](build-system)
3. Pick an issue from the [Roadmap](implementation-roadmap)

---

## Project Objectives

PetWise serves three complementary purposes:

1. **Learning Resource** - Study Clean Architecture, DDD, and modern Java/Gradle practices
2. **Reference Implementation** - See how to structure a real-world application
3. **Bootstrap Template** - Clone and adapt as a foundation for new Spring Boot projects

---

{: .warning }
> **Note:** This is an MVP (Minimum Viable Product) focused on demonstrating architectural patterns. Production deployment requires additional security, monitoring, and infrastructure considerations.

---

## Need Help?

- **Documentation Issues?** [Report here](https://github.com/deniswm/petwise-mvp/issues)
- **Questions?** [Start a discussion](https://github.com/deniswm/petwise-mvp/discussions)
- **Contributing?** Read the [Contributing Guide](contributing)

---

*Built with ❤️ as a learning resource and reference implementation*

