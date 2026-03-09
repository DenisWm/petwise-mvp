---
layout: default
title: API Reference
nav_order: 6
---

# API Reference
{: .no_toc }

PetWise generates API documentation from OpenAPI annotations in the code.
The interactive docs below are always in sync with the codebase.
{: .fs-6 .fw-300 }

## Table of contents
{: .no_toc .text-delta }

1. TOC
{:toc}

---

## Interactive Documentation

| Tool | URL (local) |
|:-----|:------------|
| **Swagger UI** | [`http://localhost:8080/api/swagger-ui.html`](http://localhost:8080/api/swagger-ui.html) |
| **Redoc** (full-screen) | [Open Redoc ↗](api/redoc.html){: target="_blank" } |
| **Raw spec** | [`docs/api/openapi.yaml`](https://github.com/deniswm/petwise-mvp/blob/master/docs/api/openapi.yaml) |

Swagger UI supports **"Try it out"** for every endpoint. Use the OAuth2 button to authenticate with Keycloak (client: `petwise-swagger`).

{: .note }
> The OpenAPI spec is **auto-generated**. Do not edit `openapi.yaml` by hand — update the `@Operation` / `@Parameter` annotations on the API interfaces, then run `./gradlew :infrastructure:generateOpenApiDocs`.

---

## Authentication

All endpoints (except health checks and docs) require a **Bearer JWT** from Keycloak.

```bash
# Obtain a token (Resource Owner Password flow — dev only)
TOKEN=$(curl -s -X POST http://localhost:9080/realms/petwise/protocol/openid-connect/token \
  -d "client_id=petwise-api" \
  -d "client_secret=petwise-dev-secret" \
  -d "grant_type=password" \
  -d "username=admin" \
  -d "password=admin123" | jq -r '.access_token')

# Use the token
curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/tutors
```

### Roles

| Role | Access |
|:-----|:-------|
| `ROLE_ADMIN` | Full platform access (ATTENDANT + TUTOR combined) |
| `ROLE_ATTENDANT` | Manage tutors, pets, appointments, daily agenda |
| `ROLE_TUTOR` | View own pets and appointment history |

### Public Endpoints (no auth)

| Path | Purpose |
|:-----|:--------|
| `GET /management/health/**` | Health & readiness probes (port 9090) |
| `GET /management/info` | Application info (port 9090) |
| `GET /api/v3/api-docs/**` | OpenAPI specification |
| `GET /api/swagger-ui/**` | Swagger UI |

---

## Error Format

All errors follow **RFC 7807 Problem Details**:

```json
{
  "type": "https://petwise.example.com/errors/not-found",
  "title": "Resource Not Found",
  "status": 404,
  "detail": "Tutor with ID 550e8400-... not found",
  "instance": "/api/tutors/550e8400-..."
}
```

| Status | Meaning |
|:-------|:--------|
| `400` | Validation error (missing/invalid fields) |
| `404` | Resource not found |
| `409` | Business rule violation (dependencies, overlap, invalid transition) |
| `500` | Unexpected server error |

---

## Further Reading

- [API Guidelines](api/guidelines) — REST conventions, pagination, date format
- [Use Cases](use-cases/) — Business requirements behind each endpoint
