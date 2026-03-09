---
layout: default
title: API Guidelines
nav_exclude: true
---

# API Guidelines

| Convention | Detail |
|:-----------|:-------|
| **Base path** | `/api` |
| **Style** | REST, resource-oriented |
| **Format** | JSON for requests and responses |
| **Validation** | Bean Validation; `400 Bad Request` for invalid input |
| **Errors** | RFC 7807 `application/problem+json` with `type`, `title`, `status`, `detail`, `instance` |
| **Pagination** | `page` (0-based), `perPage` (items per page) |
| **Dates** | ISO 8601 — `YYYY-MM-DD` for dates, `YYYY-MM-DDTHH:mm:ssZ` for timestamps |

## OpenAPI Specification

The `openapi.yaml` file is **auto-generated** from annotated controllers using Springdoc.

```bash
./gradlew :infrastructure:generateOpenApiDocs
```

This starts the application with an in-memory H2 database, fetches the spec from `/v3/api-docs.yaml`, and writes it to `docs/api/openapi.yaml`.

> **Do not edit `openapi.yaml` by hand.** Update the `@Operation`, `@ApiResponse`, and `@Parameter` annotations on the API interfaces instead, then re-generate.
