# ADR-0001 – Use SQLite as the first database

- **Status**: Accepted
- **Date**: 2025-11-13

## Context

We need a simple relational database for the MVP. Running a full Postgres instance
locally increases setup friction and is not required for the first iteration.

## Decision

Use **SQLite** as the default database for the MVP (local file, single instance).
Keep the JPA configuration compatible with later migration to Postgres.

## Consequences

**Positive**

- Very low setup cost (no external DB service required).
- Easy to run in Docker and on developer machines.
- Good enough for local development and small datasets.

**Negative**

- Not suitable for real production concurrency/load.
- Some SQL dialect differences when migrating to Postgres.
- No real multi-instance deployment support.
