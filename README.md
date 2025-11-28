# PetWise

> A pedagogical Spring Boot application demonstrating Clean Architecture, Domain-Driven Design (DDD), and modern Gradle build practices.

**PetWise** is an MVP system for managing pet daycare and hotel services. It is designed as both a **learning resource** and a **bootstrap template** for Java developers interested in well-structured, enterprise-grade applications.

---

## 🎯 Project Objectives

This project serves three complementary purposes:

1. **Learning Resource** – Study Clean Architecture, DDD tactical patterns (Aggregates, Entities, Value Objects), and use-case-driven development
2. **Reference Implementation** – See how to structure a multi-module Gradle project with custom build conventions, linting, and testing
3. **Bootstrap Template** – Clone and adapt this codebase as a foundation for new Spring Boot projects

---

## 🏗️ Architecture Overview

PetWise follows a **three-layer architecture** inspired by Clean Architecture and Hexagonal Architecture:

```
┌─────────────────┐
│ infrastructure  │  ← REST controllers, JPA persistence, Spring Boot config
├─────────────────┤
│  application    │  ← Use cases orchestrating business workflows
├─────────────────┤
│    domain       │  ← Business rules, aggregates, entities, value objects
└─────────────────┘
```

### Modules

- **domain** – Pure Java, no frameworks. Contains aggregates (`Tutor`, `Appointment`), entities (`Pet`), domain events, validation, and DDD base classes
- **application** – Use-case classes implementing business workflows. Depends only on domain
- **infrastructure** – Spring Boot web layer, JPA repositories, configuration. Depends on application and domain
- **build-logic** – Gradle convention plugins for Java, Spring Boot, linting (Spotless), and code coverage (JaCoCo)

---

## 🚀 Getting Started

### Prerequisites

- **Java 21** (JDK)
- **Docker** (optional, for diagrams and PostgreSQL)
- **Make** (optional, for rendering diagrams)

### Run Locally

```bash
# Build the project
./gradlew build

# Run the application (uses SQLite by default)
./gradlew :infrastructure:bootRun

# Access the API
curl http://localhost:8080/api/v1/tutors
```

### Run with Docker Compose

```bash
docker-compose up --build
```

This starts:
- PostgreSQL on port 5432
- Spring Boot app on port 8080

---

## 📚 Documentation

All documentation is located in the [`docs/`](docs/) folder and follows a structured approach:

| Section | What You'll Find |
|---------|------------------|
| **[docs/README.md](docs/README.md)** | Navigation guide for all documentation |
| **[docs/architecture/](docs/architecture/)** | C4 diagrams, domain model (ERD, glossary, business rules), ADRs, sequence diagrams |
| **[docs/use-cases/](docs/use-cases/)** | Detailed use case descriptions (UC-01 to UC-06) |
| **[docs/api/](docs/api/)** | OpenAPI specification and REST API guidelines |
| **[docs/diagrams/](docs/diagrams/)** | High-level use case overview |

### Key Documentation Highlights

- **C4 Diagrams** – Understand system context, containers, and components
- **ERD** – Canonical schema for entities and relationships
- **ADRs** – Architectural decision records explaining technical choices (SQLite, DDD patterns, UseCase pattern, etc.)
- **Business Rules** – Domain invariants and constraints
- **Sequence Diagrams** – Runtime interactions for each use case

---

## 🧪 Testing

```bash
# Run all tests
./gradlew test

# Run tests with coverage report
./gradlew test jacocoTestReport

# Coverage report location
# build/reports/jacoco/test/html/index.html
```

---

## 🛠️ Build System

PetWise uses **Gradle 8.14+** with **custom convention plugins** defined in the `build-logic/` module:

- `petwise.java-library-conventions` – Base Java library config (JUnit, Spotless, JaCoCo)
- `petwise.spring-boot-app-conventions` – Spring Boot application setup
- `petwise.lint-conventions` – Code formatting with Spotless (Google Java Format)
- `petwise.jacoco-conventions` – Code coverage reporting

This approach:
- Keeps build scripts DRY
- Centralizes conventions
- Makes it easy to add new modules with consistent configuration

---

## 📐 Diagrams

All `.puml` (PlantUML) diagrams can be rendered using Docker:

```bash
# Render all diagrams
make diagrams

# Render a specific diagram
make docs/architecture/c4/c4-context.png

# Clean generated PNGs
make clean
```

Rendered `.png` files are created alongside `.puml` sources.

---

## 🧩 Technology Stack

| Layer | Technologies |
|-------|-------------|
| **Backend** | Java 21, Spring Boot 3.5.7, Spring Data JPA |
| **Build** | Gradle 8.14 (Kotlin DSL), custom convention plugins |
| **Database** | SQLite (default), PostgreSQL (via Docker) |
| **Testing** | JUnit 5, Spring Boot Test |
| **Code Quality** | Spotless (Google Java Format), JaCoCo |
| **Diagrams** | PlantUML, C4 Model |
| **API** | REST, OpenAPI 3.0 |

---

## 🧠 Learning Path

If you're new to this project, we recommend the following learning path:

1. **Read [docs/README.md](docs/README.md)** – Understand documentation structure
2. **Review Use Cases** – Start with `docs/use-cases/uc-01-create-tutor.md`
3. **Explore the Domain Model** – Read `docs/architecture/domain/glossary.md` and `erd.puml`
4. **Study C4 Diagrams** – Visualize system structure (context → containers → components)
5. **Read ADRs** – Understand why key decisions were made
6. **Examine the Code** – Trace a use case from controller → use case → domain → gateway
7. **Run Tests** – See how domain logic is tested in isolation

---

## 🤝 Contributing

This is a pedagogical project, but contributions are welcome!

**For Contributors:**
- Read **[CONTRIBUTING.md](docs/CONTRIBUTING.md)** for guidelines
- Check **[Implementation Roadmap](docs/IMPLEMENTATION_ROADMAP.md)** for planned features
- See **[GitHub Project Setup](docs/GITHUB_PROJECT_SETUP.md)** for issue tracking

**Quick Start for Contributors:**
1. Follow the existing code style (enforced by Spotless)
2. Update documentation when adding features
3. Add ADRs for significant architectural changes
4. Ensure tests pass and coverage remains high
5. Run `make diagrams` if you modify `.puml` files

---

## 📄 License

This project is provided as-is for educational purposes. Feel free to use it as a template or reference for your own projects.

---

## 🙏 Acknowledgments

This project demonstrates patterns and practices learned from:

- **Clean Architecture** (Robert C. Martin)
- **Domain-Driven Design** (Eric Evans, Vaughn Vernon)
- **C4 Model** (Simon Brown)
- **Spring Boot Best Practices**
- **Gradle Best Practices**

---

**Happy Learning! 🚀**

