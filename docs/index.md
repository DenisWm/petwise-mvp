---
layout: default
title: Home
nav_order: 1
description: "PetWise – Clean Architecture & DDD reference implementation with Spring Boot 3 and Java 21."
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

- [Architecture Overview](architecture/overview) — Layers, modules, patterns and decisions
- [Domain Model](architecture/domain) — ERD, glossary, business rules
- [Use Cases](use-cases/) — UC-01 through UC-06
- [API Reference](api-reference) — REST endpoints and OpenAPI spec
- [Build System](build-system) — Gradle convention plugins
- [Operations Guide](operations) — Logging, actuator, runtime debugging

</div>

<div class="code-example" markdown="1">

### 🤝 Contributing

- [**Contributing Guide**](contributing) — How to contribute

</div>

---

## Technology Stack

| Layer | Technologies |
|:------|:-------------|
| **Backend** | Java 21, Spring Boot 3.5.7, Spring Data JPA, Spring Security (OAuth2 Resource Server) |
| **Build** | Gradle 8.14 (Kotlin DSL), convention plugins |
| **Database** | PostgreSQL (Docker Compose), H2 (tests), Flyway migrations |
| **Identity** | Keycloak 26.0 (Docker Compose) |
| **Web Server** | Undertow |
| **Testing** | JUnit 5, Mockito, AssertJ, Spring Boot Test, JaCoCo |
| **Quality** | Spotless, SpotBugs, PMD, Checkstyle, OWASP Dependency-Check |
| **Diagrams** | PlantUML, C4 Model |
| **API Docs** | OpenAPI 3.0, Springdoc, Swagger UI, Redoc |

---

## Project Objectives

PetWise serves three complementary purposes:

1. **Learning Resource** - Study Clean Architecture, DDD, and modern Java/Gradle practices
2. **Reference Implementation** - See how to structure a real-world application
3. **Bootstrap Template** - Clone and adapt as a foundation for new Spring Boot projects

---

{: .warning }
> This is an MVP focused on demonstrating architectural patterns. Production deployment requires additional security, monitoring, and infrastructure considerations.

---

*Built with ❤️ as a learning resource and reference implementation*

