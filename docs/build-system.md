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

## Overview

PetWise uses a **multi-module Gradle** setup with three layers:

```
┌─────────────────┐      ┌──────────────┐      ┌────────┐
│  infrastructure │ ───▶ │  application │ ───▶ │ domain │
│  (Spring Boot)  │      │  (use cases) │      │ (core) │
└─────────────────┘      └──────────────┘      └────────┘
   spring-boot-app          java-library         java-library
    -conventions             -conventions         -conventions
```

Each arrow means "depends on". The **domain** module has zero framework dependencies;
**application** orchestrates use cases; **infrastructure** wires everything with Spring Boot.

---

## Why Convention Plugins?

Multi-module projects tend to repeat the same configuration in every `build.gradle.kts`.
PetWise avoids this with **convention plugins** inside a **composite build** (`build-logic/`),
so each module's build file stays minimal while sharing a single set of rules.

> **All plugin sources live in** `build-logic/src/main/kotlin/`.

### Available plugins

| Plugin | What it does | Applied to |
|:-------|:-------------|:-----------|
| `petwise.java-library-conventions` | Java 21 toolchain · JUnit 5 · Spotless · JaCoCo | `domain`, `application` |
| `petwise.spring-boot-app-conventions` | Everything above **+** Spring Boot · custom JAR name | `infrastructure` |
| `petwise.lint-conventions` | Google Java Format via Spotless | _(applied transitively)_ |
| `petwise.jacoco-conventions` | Code-coverage reporting | _(applied transitively)_ |
| `petwise.owasp-dependency-check-conventions` | CVE scanning of dependencies | _(applied transitively)_ |

---

## Version Catalog

All dependency versions live in **one place**: `gradle/libs.versions.toml`.

Modules reference dependencies by their **type-safe alias** instead of raw coordinates:

```kotlin
// ✅  Use the catalog alias
implementation(libs.spring.boot.starter.web)

// ❌  Avoid hard-coded coordinates
implementation("org.springframework.boot:spring-boot-starter-web:3.x.x")
```

---

## Common Tasks

| What you want to do | Command |
|:---------------------|:--------|
| Build all modules | `./gradlew build` |
| Run all tests | `./gradlew test` |
| Tests **+** coverage report | `./gradlew test jacocoTestReport` |
| Auto-format code | `./gradlew spotlessApply` |
| CVE scan | `./gradlew dependencyCheckAnalyze` |
| Run the application | `./gradlew :infrastructure:bootRun` |
| Regenerate OpenAPI spec | `./gradlew :infrastructure:generateOpenApiDocs` |

---

## Adding a New Module

Follow these four steps to add a module (e.g. `new-module`):

**1 — Create the directory layout**

```bash
mkdir -p new-module/src/main/java new-module/src/test/java
```

**2 — Add a `build.gradle.kts`**

```kotlin
// new-module/build.gradle.kts
plugins {
    alias(libs.plugins.java.library.convention)
}

dependencies {
    implementation(project(":domain"))
}
```

**3 — Register the module in `settings.gradle.kts`**

```kotlin
include("new-module")
```

**4 — Verify the build**

```bash
./gradlew :new-module:build
```

---

## Further Reading

- [Gradle Convention Plugins](https://docs.gradle.org/current/samples/sample_convention_plugins.html)
- [Gradle Version Catalogs](https://docs.gradle.org/current/userguide/platforms.html)
- [Gradle Composite Builds](https://docs.gradle.org/current/userguide/composite_builds.html)
