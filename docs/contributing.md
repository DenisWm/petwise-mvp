---
layout: default
title: Contributing
nav_order: 8
---

# Contributing to PetWise
{: .no_toc }

Thank you for your interest in contributing to PetWise!
{: .fs-6 .fw-300 }

## Table of contents
{: .no_toc .text-delta }

1. TOC
{:toc}

---

## Project Philosophy

PetWise prioritizes **clarity over cleverness**. Contributions should be well-documented, properly tested, and aligned with existing [Architecture Decision Records](architecture/decisions).

---

## Development Setup

### Prerequisites

- Java 21 JDK
- Docker (for PostgreSQL, Keycloak, and diagrams)
- Make (optional, for diagram rendering)

### Clone and Build

```bash
git clone https://github.com/deniswm/petwise-mvp.git
cd petwise
./gradlew build
```

### Run Tests

```bash
./gradlew test
```

### Apply Code Formatting

```bash
./gradlew spotlessApply
```

---

## Contribution Workflow

### 1. Choose What to Contribute

**Good first contributions:**
- Fix typos or improve documentation
- Add missing tests
- Improve code comments
- Add examples to existing documentation

**Larger contributions:**
- Implement a new use case
- Add new domain features
- Improve build configuration
- Enhance testing infrastructure

### 2. Create a Branch

```bash
git checkout -b feature/your-feature-name
# or
git checkout -b fix/your-fix-name
```

### 3. Make Your Changes

Follow the [Architecture Guidelines](#architecture-guidelines) below.

### 4. Test Your Changes

```bash
# Run all tests
./gradlew test

# Check code formatting
./gradlew spotlessCheck

# Apply formatting if needed
./gradlew spotlessApply

# Generate coverage report
./gradlew test jacocoTestReport
```

### 5. Update Documentation

If your change affects:
- **Architecture** → Update diagrams and ADRs
- **API** → Update annotations on API interfaces, then regenerate: `./gradlew :infrastructure:generateOpenApiDocs`
- **Use Cases** → Update use case docs and sequence diagrams
- **Domain Model** → Update ERD, business rules & glossary

### 6. Commit Your Changes

Use clear, descriptive commit messages:

```bash
# Good
git commit -m "feat: add validation for tutor email format"
git commit -m "docs: update appointment overlap prevention in ADR"
git commit -m "fix: correct typo in UC-03 documentation"

# Bad
git commit -m "fix stuff"
git commit -m "updates"
```

### 7. Submit a Pull Request

- Provide a clear description
- Reference related issues
- Ensure all tests pass
- Ensure code is formatted

---

## Code Style

PetWise uses **Google Java Format** enforced by **Spotless**.

**Before committing:**

```bash
./gradlew spotlessApply
```

**Key conventions:**
- 2-space indentation (enforced by formatter)
- Clear, descriptive variable names
- Javadoc for public APIs
- Package-private by default

---

## Architecture Guidelines

### Domain Layer

✅ **DO:**
- Pure Java, no framework dependencies
- Business logic and invariants
- Domain events, aggregates, entities, value objects

❌ **DON'T:**
- Spring annotations
- JPA annotations
- HTTP, JSON, or infrastructure concerns

**Example:**
```java
public class Tutor extends AggregateRoot<TutorID> {
    private final String name;
    private final Email email;

    public static Tutor newTutor(String name, String email) {
        // Validate business rules
        // Create aggregate
        return new Tutor(...);
    }
}
```

### Application Layer

✅ **DO:**
- Use case orchestration
- Depend on domain and gateway interfaces
- Input/output DTOs for boundaries

❌ **DON'T:**
- Framework dependencies
- Persistence details
- HTTP details

### Infrastructure Layer

✅ **DO:**
- Spring Boot setup
- REST controllers
- JPA repositories
- Gateway implementations

---

## Testing Guidelines

### Domain Tests
- Pure unit tests, no mocks
- Test business rules
- Fast and isolated

### Application Tests
- Test use case orchestration
- Mock gateways
- Focus on workflow

### Infrastructure Tests
- Integration tests with `@SpringBootTest`
- Test REST endpoints and repositories

---

## Pull Request Checklist

Before submitting:

- [ ] Code compiles: `./gradlew build`
- [ ] All tests pass: `./gradlew test`
- [ ] Code is formatted: `./gradlew spotlessApply`
- [ ] Documentation is updated
- [ ] Diagrams regenerated (if applicable)
- [ ] ADR added (if architectural change)
- [ ] Commit messages are clear
- [ ] PR description explains what and why

---

## Questions?

- Review existing documentation
- Check ADRs for architectural context
- Look at existing code for patterns
- [Open a discussion](https://github.com/deniswm/petwise-mvp/discussions)

---

**Thank you for contributing to PetWise!** 🚀

