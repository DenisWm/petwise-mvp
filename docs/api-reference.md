---
layout: default
title: API Reference
nav_order: 7
---

# API Quick Reference

This page provides a concise reference to the PetWise REST API. The authoritative contract is `docs/api/openapi.yaml` (OpenAPI 3). Integrators should rely on the OpenAPI file for full schemas and examples.

Base URL: http://localhost:8080/api/v1

Health
GET /actuator/health — Service health (200 OK)

Tutors
- POST /api/v1/tutors — Create tutor (201)
- GET /api/v1/tutors — List tutors (paginated)
- GET /api/v1/tutors/{id} — Retrieve tutor (200 / 404)
- PUT /api/v1/tutors/{id} — Update tutor (200)
- DELETE /api/v1/tutors/{id} — Delete tutor (204 / 409)

Pets
- POST /api/v1/pets — Create pet (201)
- GET /api/v1/pets — List or filter by tutorId
- GET /api/v1/pets/{id} — Retrieve pet (200 / 404)
- PUT /api/v1/pets/{id} — Update pet (200)
- DELETE /api/v1/pets/{id} — Delete pet (204 / 409)

Appointments
- POST /api/v1/appointments — Create appointment (201)
- PATCH /api/v1/appointments/{id}/status — Change status (200)
- GET /api/v1/appointments — List / filter by date/status
- GET /api/v1/appointments/{id} — Retrieve appointment (200 / 404)

Error format
Errors use RFC 7807 Problem Details with fields: type, title, status, detail, instance, timestamp.

Example curl (create tutor):

curl -X POST http://localhost:8080/api/v1/tutors \
  -H "Content-Type: application/json" \
  -d '{"name":"Alice Smith","email":"alice@example.com"}'

For complete schemas and examples consult `docs/api/openapi.yaml` and `docs/api/guidelines.md`.
