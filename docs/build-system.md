---
layout: default
title: Build System
nav_order: 5
---

# Build System
{: .no_toc }

How PetWise's Gradle build is organized and how to extend it.
{: .fs-6 .fw-300 }

## Table of contents
{: .no_toc .text-delta }

1. TOC
{:toc}

---

## Why Convention Plugins?

Multi-module Gradle projects tend to repeat the same configuration in every `build.gradle.kts`.
PetWise solves this with **convention plugins** inside a **composite build** (`build-logic/`), so
each module's build file stays minimal while sharing a single set of rules.

```
infrastructure → application → domain
     │                │           │
     └── spring-boot  └── java-library ──┘
         -app-conventions  -conventions
```

---

## Convention Plugins

| Plugin | Purpose | Used by |
|:-------|:--------|:--------|
| `petwise.java-library-conventions` | Java 21 toolchain, JUnit 5, Spotless, JaCoCo | `domain`, `application` |
| `petwise.spring-boot-app-conventions` | Spring Boot + all above, custom JAR name | `infrastructure` |
| `petwise.lint-conventions` | Google Java Format via Spotless | (applied transitively) |
| `petwise.jacoco-conventions` | Code coverage reporting | (applied transitively) |
| `petwise.owasp-dependency-check-conventions` | CVE scanning of dependencies | (applied transitively) |

All plugin sources live in `build-logic/src/main/kotlin/`.

---

## Version Catalog

Dependency versions are centralized in `gradle/libs.versions.toml`. Modules reference dependencies
by type-safe alias (e.g., `libs.spring.boot.starter.web`) instead of raw coordinates.

---

## Common Tasks

```bash
./gradlew build                              # Build all modules
./gradlew test                               # Run all tests
./gradlew test jacocoTestReport              # Tests + coverage report
./gradlew spotlessApply                      # Auto-format code
./gradlew dependencyCheckAnalyze             # CVE scan
./gradlew :infrastructure:bootRun            # Run the application
./gradlew :infrastructure:generateOpenApiDocs # Regenerate OpenAPI spec
```

---

## Adding a New Module

1. Create the directory and source layout:

   ```bash
   mkdir -p new-module/src/main/java new-module/src/test/java
   ```

2. Create `new-module/build.gradle.kts`:

   ```kotlin
   plugins {
       alias(libs.plugins.java.library.convention)
   }
   dependencies {
       implementation(project(":domain"))
   }
   ```

3. Register in `settings.gradle.kts`:

   ```kotlin
   include("new-module")
   ```

4. Build: `./gradlew :new-module:build`

---

## Further Reading

- [Gradle Convention Plugins](https://docs.gradle.org/current/samples/sample_convention_plugins.html)
- [Gradle Version Catalogs](https://docs.gradle.org/current/userguide/platforms.html)
- [Gradle Composite Builds](https://docs.gradle.org/current/userguide/composite_builds.html)
