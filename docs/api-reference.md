---
layout: default
title: API Reference
nav_order: 7
---

# API Quick Reference
{: .no_toc }

Quick reference for PetWise REST API endpoints.
{: .fs-6 .fw-300 }

{: .note }
> **Complete Specification:** See [OpenAPI YAML](https://github.com/deniswm/petwise-mvp/blob/master/docs/api/openapi.yaml)

## Table of contents
{: .no_toc .text-delta }

1. TOC
{:toc}

---

## Base URL

```
http://localhost:8080/api/v1
```

---

## Health

### Health Check

```http
GET /actuator/health
```

**Response:** `200 OK`
```json
{
  "status": "UP"
}
```

---

## Tutors

### Create Tutor

```http
POST /api/v1/tutors
Content-Type: application/json

{
  "name": "Alice Smith",
  "email": "alice@example.com",
  "phone": "+1234567890"
}
```

**Response:** `201 Created`
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Alice Smith",
  "email": "alice@example.com",
  "phone": "+1234567890",
  "createdAt": "2025-11-27T10:00:00Z",
  "updatedAt": "2025-11-27T10:00:00Z"
}
```

### List Tutors

```http
GET /api/v1/tutors?page=0&size=20
```

**Response:** `200 OK` (paginated)

### Get Tutor by ID

```http
GET /api/v1/tutors/{id}
```

**Response:** `200 OK` or `404 Not Found`

### Update Tutor

```http
PUT /api/v1/tutors/{id}
Content-Type: application/json

{
  "name": "Alice Johnson",
  "email": "alice.johnson@example.com",
  "phone": "+1234567890"
}
```

**Response:** `200 OK`

### Delete Tutor

```http
DELETE /api/v1/tutors/{id}
```

**Response:** `204 No Content` or `409 Conflict` (if has pets)

---

## Pets

### Create Pet

```http
POST /api/v1/pets
Content-Type: application/json

{
  "tutorId": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Fluffy",
  "species": "Cat",
  "breed": "Persian",
  "birthDate": "2020-03-15",
  "notes": "Allergic to chicken"
}
```

**Response:** `201 Created`

### List Pets

```http
GET /api/v1/pets?tutorId={tutorId}&page=0&size=20
```

**Query Parameters:**
- `tutorId` (optional) - Filter by tutor
- `page` (optional) - Page number (default: 0)
- `size` (optional) - Page size (default: 20)

**Response:** `200 OK` (paginated)

### Get Pet by ID

```http
GET /api/v1/pets/{id}
```

**Response:** `200 OK` or `404 Not Found`

### Update Pet

```http
PUT /api/v1/pets/{id}
```

**Response:** `200 OK`

### Delete Pet

```http
DELETE /api/v1/pets/{id}
```

**Response:** `204 No Content` or `409 Conflict` (if has active appointments)

---

## Appointments

### Create Appointment

```http
POST /api/v1/appointments
Content-Type: application/json

{
  "petId": "660e8400-e29b-41d4-a716-446655440001",
  "serviceType": "CRECHE",
  "startAt": "2025-11-28T08:00:00Z",
  "endAt": "2025-11-28T18:00:00Z",
  "notes": "First time at daycare"
}
```

**Service Types:**
- `CRECHE` - Daycare
- `HOTEL` - Hotel/boarding

**Response:** `201 Created`
```json
{
  "id": "770e8400-e29b-41d4-a716-446655440002",
  "petId": "660e8400-e29b-41d4-a716-446655440001",
  "serviceType": "CRECHE",
  "status": "PENDING",
  "startAt": "2025-11-28T08:00:00Z",
  "endAt": "2025-11-28T18:00:00Z",
  "notes": "First time at daycare"
}
```

**Errors:**
- `404 Not Found` - Pet not found
- `400 Bad Request` - Invalid date range (startAt >= endAt)
- `409 Conflict` - Overlapping appointment for same pet

### Change Appointment Status

```http
PATCH /api/v1/appointments/{id}/status
Content-Type: application/json

{
  "status": "ACTIVE"
}
```

**Valid Status Transitions:**

PENDING → ACTIVE
{: .label .label-green }

ACTIVE → COMPLETED
{: .label .label-blue }

PENDING → CANCELED
{: .label .label-red }

**Response:** `200 OK`

**Errors:**
- `404 Not Found` - Appointment not found
- `409 Conflict` - Invalid status transition

### Get Appointment by ID

```http
GET /api/v1/appointments/{id}
```

**Response:** `200 OK` or `404 Not Found`

### View Daily Agenda

```http
GET /api/v1/appointments?date=2025-11-28&status=ACTIVE&serviceType=CRECHE
```

**Query Parameters:**
- `date` (required) - Date in `YYYY-MM-DD` format
- `status` (optional) - `PENDING`, `ACTIVE`, `COMPLETED`, `CANCELED`
- `serviceType` (optional) - `CRECHE`, `HOTEL`
- `page` (optional) - Page number
- `size` (optional) - Page size

**Response:** `200 OK` (paginated)

---

## Error Responses

All errors follow **RFC 7807 Problem Details** format:

```json
{
  "type": "https://petwise.example.com/errors/not-found",
  "title": "Resource Not Found",
  "status": 404,
  "detail": "Tutor with ID 550e8400-... not found",
  "instance": "/api/v1/tutors/550e8400-...",
  "timestamp": "2025-11-27T10:15:00Z"
}
```

### Common Error Codes

| Status | Description | When |
|:-------|:------------|:-----|
| `400 Bad Request` | Validation error | Invalid input, missing required fields |
| `404 Not Found` | Resource not found | ID doesn't exist |
| `409 Conflict` | Business rule violation | Cannot delete (has dependencies), overlapping appointment, invalid status transition |
| `500 Internal Server Error` | Server error | Unexpected error |

---

## Testing with curl

```bash
# Create a tutor
curl -X POST http://localhost:8080/api/v1/tutors \
  -H "Content-Type: application/json" \
  -d '{"name":"Alice Smith","email":"alice@example.com"}'

# List tutors
curl http://localhost:8080/api/v1/tutors

# Create a pet (replace TUTOR_ID)
curl -X POST http://localhost:8080/api/v1/pets \
  -H "Content-Type: application/json" \
  -d '{"tutorId":"TUTOR_ID","name":"Fluffy","species":"Cat"}'

# View today's agenda
curl "http://localhost:8080/api/v1/appointments?date=$(date +%Y-%m-%d)"
```

---

## Further Reading

- [API Guidelines](https://github.com/deniswm/petwise-mvp/blob/master/docs/api/guidelines.md)
- [OpenAPI Specification](https://github.com/deniswm/petwise-mvp/blob/master/docs/api/openapi.yaml)
- [Use Cases](use-cases/)

