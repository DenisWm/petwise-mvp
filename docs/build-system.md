---
layout: default
title: Build System
nav_order: 6
---

# Build System Guide
{: .no_toc }

PetWise uses Gradle convention plugins and a Kotlin DSL-based multi-module build. This guide explains the structure and common tasks.
{: .fs-6 .fw-300 }

## Overview

PetWise uses Gradle 8.14+ with the Kotlin DSL and convention plugins to centralize build configuration and reduce duplication across modules.

## Project structure

```
petwise/
├── build-logic/                   # Convention plugins (composite build)
├── domain/                        # Java library
├── application/                   # Java library
├── infrastructure/                # Spring Boot app
├── gradle/                        # Version catalog
└── settings.gradle.kts
```

## Version catalog

A version catalog (`gradle/libs.versions.toml`) centralizes dependency versions and bundles. Use `libs` aliases in module build files for consistency and type safety.

## Convention plugins

Purpose: provide shared configuration for modules (toolchain, testing, formatting, coverage).

Examples:
- `java-library-conventions` — Java library configuration (Java toolchain, JUnit, Spotless, JaCoCo)
- `spring-boot-app-conventions` — Spring Boot application configuration
- `lint-conventions` — Spotless formatting rules
- `jacoco-conventions` — Coverage reporting

## Common tasks

Build:
```bash
./gradlew build
./gradlew :domain:build
```

Test:
```bash
./gradlew test
./gradlew :domain:test
./gradlew test jacocoTestReport
```

Run application:
```bash
./gradlew :infrastructure:bootRun
```

Formatting:
```bash
./gradlew spotlessCheck
./gradlew spotlessApply
```

## Adding a new module

1. Create module directory and source folders
2. Add a minimal `build.gradle.kts` using convention plugins
3. Register the module in `settings.gradle.kts`
4. Build the module

## Composite build (`build-logic`)

`build-logic/` contains convention plugin implementations as a composite build. Benefits include pre-built plugins, type-safe references, and IDE support.

## Best practices

- Use the version catalog (`libs`) rather than hard-coded dependency coordinates
- Keep module build files minimal and rely on convention plugins
- Apply formatting and run tests before committing

## Troubleshooting

Plugin not found:
- Ensure `build-logic` is included in `settings.gradle.kts`
- Rebuild: `./gradlew clean build`

Version catalog issues:
- Verify `gradle/libs.versions.toml` exists
- Refresh dependencies: `./gradlew --refresh-dependencies`

## Further reading

- Gradle Version Catalogs: https://docs.gradle.org/current/userguide/platforms.html
- Gradle Convention Plugins: https://docs.gradle.org/current/samples/sample_convention_plugins.html
- Gradle Composite Builds: https://docs.gradle.org/current/userguide/composite_builds.html
