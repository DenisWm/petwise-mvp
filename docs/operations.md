---
layout: default
title: Operations Guide
nav_order: 8
description: "Runtime observability, logging, and management for PetWise"
---

# Operations Guide
{: .no_toc }

Runtime observability, structured logging, and management endpoints.
{: .fs-6 .fw-300 }

## Table of contents
{: .no_toc .text-delta }

1. TOC
{:toc}

---

## Logging Architecture

PetWise uses **SLF4J + Logback** across all modules. The `domain` and `application` modules depend
only on `slf4j-api` (framework-agnostic), while the `infrastructure` module configures Logback
through `logback-spring.xml`.

### Profiles

| Profile               | Console format     | File output       | App log level | Root level |
|-----------------------|--------------------|-------------------|---------------|------------|
| `default`/`development` | Human-readable text | —                 | `DEBUG`       | `INFO`     |
| `production`          | Structured JSON    | Structured JSON (rolling) | `INFO`  | `WARN`     |
| `test-integration`    | Human-readable text | —                 | `DEBUG`       | `WARN`     |

### Structured JSON (Production)

In production, all log output uses the [Logstash Logback Encoder](https://github.com/logfellow/logstash-logback-encoder),
which produces JSON lines that include:

- Timestamp, level, logger name, message
- Full stack traces (as nested JSON)
- **All MDC fields** (`requestId`, `method`, `uri`) automatically
- Custom field `"app": "petwise"`

This format is ready for ingestion by ELK, Loki, Datadog, or any JSON-aware log aggregator.

### Log Severity Conventions

| Level     | When to use                                              | Examples                                           |
|-----------|----------------------------------------------------------|----------------------------------------------------|
| **TRACE** | Method entry/exit, raw payloads, filter lifecycle        | Request correlation filter start, full request body |
| **DEBUG** | Internal decisions, input summaries, HTTP method logging | `POST /tutors`, `Persisting tutor id=X`            |
| **INFO**  | Business milestones, startup events                      | `Tutor created id=X`, `Appointment status changed`  |
| **WARN**  | Recoverable problems, validation failures                | `NotificationException`, `NotFoundException`        |
| **ERROR** | Unexpected/unrecoverable failures                        | Catch-all in `GlobalExceptionHandler`, DB down      |

### MDC Context

Every HTTP request automatically carries these MDC fields (set by `RequestCorrelationFilter`):

| MDC Key     | Description                                  |
|-------------|----------------------------------------------|
| `requestId` | Short UUID or client-supplied `X-Request-ID` |
| `method`    | HTTP method (`GET`, `POST`, etc.)            |
| `uri`       | Request URI path                             |

The `requestId` is also returned in the `X-Request-ID` response header so clients can correlate
their requests with server-side logs.

---

## Actuator Endpoints

PetWise exposes Spring Boot Actuator on a **separate management port** (`9090`) to keep it
isolated from the public API:

| Endpoint                         | Method | Description                        |
|----------------------------------|--------|------------------------------------|
| `GET /management/health`         | GET    | Application health check           |
| `GET /management/info`           | GET    | Application info                   |
| `GET /management/loggers`        | GET    | List all loggers and their levels  |
| `GET /management/loggers/{name}` | GET    | Read a specific logger's level     |
| `POST /management/loggers/{name}`| POST   | Change a logger's level at runtime |

{: .warning }
> The management port (`9090`) should **never** be exposed to the public internet. Configure your
> load balancer / firewall to block external access.

---

## Runtime Debug Activation (Without Restart)

The `/management/loggers` endpoint allows you to change log levels **at runtime** while the
application is running in production. This is invaluable for investigating issues without
restarting or redeploying.

### Read Current Level

```bash
curl http://localhost:9090/management/loggers/com.petwise
```

Response:
```json
{
  "configuredLevel": "INFO",
  "effectiveLevel": "INFO"
}
```

### Activate DEBUG for All PetWise Code

```bash
curl -X POST http://localhost:9090/management/loggers/com.petwise \
  -H 'Content-Type: application/json' \
  -d '{"configuredLevel": "DEBUG"}'
```

### Activate DEBUG for a Specific Class

```bash
curl -X POST http://localhost:9090/management/loggers/com.petwise.infrastructure.appointment.api.AppointmentController \
  -H 'Content-Type: application/json' \
  -d '{"configuredLevel": "DEBUG"}'
```

### Enable SQL Logging

```bash
curl -X POST http://localhost:9090/management/loggers/org.hibernate.SQL \
  -H 'Content-Type: application/json' \
  -d '{"configuredLevel": "DEBUG"}'
```

### Reset to Default Level

```bash
curl -X POST http://localhost:9090/management/loggers/com.petwise \
  -H 'Content-Type: application/json' \
  -d '{"configuredLevel": null}'
```

{: .note }
> Changes made via the loggers endpoint are **ephemeral** — they are lost when the application
> restarts. This is by design: production defaults are always restored from `logback-spring.xml`.

---

## Log File Management (Production)

In the `production` profile, logs are written to `logs/petwise.log` with the following rolling policy:

- **Max file size:** 50 MB per file
- **Max history:** 30 days
- **Total size cap:** 1 GB
- **Archive pattern:** `logs/petwise.YYYY-MM-DD.N.log.gz` (compressed)

---

## Correlation & Tracing

1. **Client sends** `X-Request-ID: my-trace-123` header (optional)
2. **Server** reuses the value or generates a short UUID
3. **Every log line** includes the `requestId` (text pattern) or `requestId` MDC field (JSON)
4. **Response** includes `X-Request-ID: my-trace-123` header
5. **Client** can use the ID to search logs: `grep "my-trace-123" logs/petwise.log`

