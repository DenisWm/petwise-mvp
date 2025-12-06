---
layout: default
title: Contributing
nav_order: 8
---

# Contributing

We welcome improvements that increase clarity, tests, or production readiness. Follow this concise workflow when contributing code or documentation.

## Prerequisites

- Java 21 JDK
- Git
- (Optional) Docker for integration testing

## Workflow

1. Fork the repository and create a descriptive branch: `feature/issue-123-summary`.
2. Implement changes, include tests for behavior, and run formatting.
3. Run the project's checks locally before opening a PR.

## Required checks

- Build: `./gradlew build`
- Tests: `./gradlew test`
- Formatting: `./gradlew spotlessCheck` (use `spotlessApply` to fix)

## Pull request checklist

- Tests added for new behavior
- Code formatted and lint clean
- Documentation updated where behavior or public APIs change
- CI green
- PR description includes rationale and testing steps

## Code style

- Google Java Format (Spotless)
- Prefer package-private visibility for implementation details
- Document public APIs with Javadoc

If you need guidance, open a discussion on the project repository before investing large changes.
