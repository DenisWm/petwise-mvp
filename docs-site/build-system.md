---
layout: default
title: Build System
nav_order: 6
---

# Build System Guide
{: .no_toc }

PetWise's Gradle build system, convention plugins, and how to maintain and extend the build configuration.
{: .fs-6 .fw-300 }

## Table of contents
{: .no_toc .text-delta }

1. TOC
{:toc}

---

## Overview

PetWise uses **Gradle 8.14+** with the **Kotlin DSL** and a **custom convention plugin** architecture.

### Why This Approach?

**Problem:** Traditional multi-module projects repeat the same configuration in every `build.gradle.kts`

**Solution:** PetWise uses **convention plugins** to centralize this configuration.

---

## Project Structure

```
petwise/
├── build-logic/                   # Convention plugins (composite build)
│   ├── build.gradle.kts
│   ├── settings.gradle.kts
│   └── src/main/kotlin/
│       ├── petwise.java-library-conventions.gradle.kts
│       ├── petwise.spring-boot-app-conventions.gradle.kts
│       ├── petwise.lint-conventions.gradle.kts
│       └── petwise.jacoco-conventions.gradle.kts
│
├── domain/                        # Uses java-library-conventions
├── application/                   # Uses java-library-conventions
├── infrastructure/                # Uses spring-boot-app-conventions
│
├── gradle/
│   └── libs.versions.toml        # Version catalog
│
└── settings.gradle.kts
```

---

## Version Catalog

PetWise uses a **version catalog** (`gradle/libs.versions.toml`) to centralize dependency versions.

### Structure

```toml
[versions]
spring-boot = "3.5.7"
junit = "5.10.2"

[libraries]
spring-boot-starter-web = { module = "org.springframework.boot:spring-boot-starter-web", version.ref = "spring-boot" }
junit-jupiter = { module = "org.junit.jupiter:junit-jupiter", version.ref = "junit" }

[bundles]
spring-boot-starter = ["spring-boot-starter-web", "spring-boot-starter-validation"]

[plugins]
spring-boot-app-convention = { id = "petwise.spring-boot-app-conventions" }
java-library-convention = { id = "petwise.java-library-conventions" }
```

### Usage

```kotlin
dependencies {
    // Single dependency
    implementation(libs.spring.boot.starter.web)
    
    // Bundle
    implementation(libs.bundles.spring.boot.starter)
    
    // Test dependency
    testImplementation(libs.junit.jupiter)
}

plugins {
    alias(libs.plugins.java.library.convention)
}
```

{: .highlight }
> **Benefits:** Single source of truth, type-safe references, easy updates, prevents conflicts

---

## Convention Plugins

### 1. java-library-conventions

**Purpose:** Base configuration for Java library modules (domain, application)

**Includes:**
- Java 21 toolchain
- JUnit 5
- Spotless (formatting)
- JaCoCo (coverage)

**Used by:** `domain`, `application`

### 2. spring-boot-app-conventions

**Purpose:** Spring Boot application module configuration

**Includes:**
- Spring Boot plugin
- Spring Dependency Management
- Java 21 toolchain
- Lint and JaCoCo conventions
- Custom JAR name: `petwise-application.jar`

**Used by:** `infrastructure`

### 3. lint-conventions

**Purpose:** Code formatting with Spotless

**Configuration:**
- Google Java Format
- Trim trailing whitespace
- End with newline

**Commands:**
```bash
./gradlew spotlessCheck    # Check formatting
./gradlew spotlessApply    # Apply formatting
```

### 4. jacoco-conventions

**Purpose:** Code coverage reporting

**Commands:**
```bash
./gradlew test jacocoTestReport
# Report: build/reports/jacoco/test/html/index.html
```

---

## Module Build Files

With convention plugins, module build files are minimal:

### Domain Module

```kotlin
plugins {
    alias(libs.plugins.java.library.convention)
}
```

That's it! The convention plugin provides Java 21, JUnit 5, Spotless, and JaCoCo.

### Infrastructure Module

```kotlin
plugins {
    alias(libs.plugins.spring.boot.app.convention)
}

dependencies {
    implementation(project(":application"))
    implementation(project(":domain"))
    
    implementation(libs.bundles.spring.boot.starter)
    implementation(libs.spring.boot.starter.web)
    
    runtimeOnly(libs.postgresql)
    
    testImplementation(libs.spring.boot.starter.test)
}
```

---

## Common Gradle Tasks

### Build

```bash
# Build all modules
./gradlew build

# Build specific module
./gradlew :domain:build
```

### Test

```bash
# Run all tests
./gradlew test

# Run tests for specific module
./gradlew :domain:test

# With coverage
./gradlew test jacocoTestReport
```

### Clean

```bash
./gradlew clean
```

### Run Application

```bash
./gradlew :infrastructure:bootRun
```

### Code Formatting

```bash
./gradlew spotlessCheck
./gradlew spotlessApply
```

---

## Adding a New Module

### Step 1: Create Directory

```bash
mkdir new-module
mkdir -p new-module/src/main/java
mkdir -p new-module/src/test/java
```

### Step 2: Create build.gradle.kts

```kotlin
plugins {
    alias(libs.plugins.java.library.convention)
}

dependencies {
    implementation(project(":domain"))
}
```

### Step 3: Register in settings.gradle.kts

```kotlin
include("domain")
include("application")
include("infrastructure")
include("new-module")  // Add this
```

### Step 4: Build

```bash
./gradlew :new-module:build
```

---

## Composite Build (build-logic)

The `build-logic/` folder is a **composite build** – a separate Gradle project that provides plugins.

### Benefits

- ✅ Convention plugins built before main project
- ✅ Type-safe plugin references
- ✅ Can be versioned independently
- ✅ IDE support (autocomplete, refactoring)

---

## Best Practices

### 1. Use Version Catalog

❌ **Don't:**
```kotlin
implementation("org.springframework.boot:spring-boot-starter-web:3.5.7")
```

✅ **Do:**
```kotlin
implementation(libs.spring.boot.starter.web)
```

### 2. Keep Build Files Minimal

Let convention plugins handle common configuration.

### 3. Apply Formatting

```bash
./gradlew spotlessApply
```

### 4. Test Changes

```bash
./gradlew clean build
```

---

## Troubleshooting

### Plugin Not Found

**Solution:**
1. Ensure `build-logic` is included in `settings.gradle.kts`
2. Rebuild: `./gradlew clean build`

### Version Catalog Not Resolved

**Solution:**
1. Check `gradle/libs.versions.toml` exists
2. Sync: `./gradlew --refresh-dependencies`

---

## Summary

PetWise's build system demonstrates:

✅ **Convention Plugins** – DRY build configuration  
✅ **Version Catalog** – Centralized dependency management  
✅ **Composite Build** – Type-safe, IDE-friendly plugins  
✅ **Code Quality** – Automated formatting and coverage  
✅ **Multi-Module** – Clear separation of concerns  

---

## Further Reading

- [Gradle Version Catalogs](https://docs.gradle.org/current/userguide/platforms.html)
- [Gradle Convention Plugins](https://docs.gradle.org/current/samples/sample_convention_plugins.html)
- [Gradle Composite Builds](https://docs.gradle.org/current/userguide/composite_builds.html)

