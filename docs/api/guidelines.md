# API Guidelines (PetWise)

- Base path: `/api`
- Style: REST, resource-oriented
- Format: JSON for requests and responses
- Validation:
    - Bean Validation in the backend
    - 400 Bad Request for validation errors
- Errors:
    - Use `application/problem+json` (RFC 7807)
    - Include `type`, `title`, `status`, `detail`, `instance`
- Pagination:
    - `page` (0-based), `perPage` (items per page)
- Date/time:
    - `date`: ISO 8601 (`YYYY-MM-DD`)
    - `date-time`: ISO 8601 with timezone (`YYYY-MM-DDTHH:mm:ssZ` or offset)

## OpenAPI Specification

The `openapi.yaml` file is **auto-generated** from the annotated controllers using Springdoc.

```bash
./gradlew :infrastructure:generateOpenApiDocs
```

This starts the application with an in-memory H2 database, fetches the spec from
`/v3/api-docs.yaml`, and writes it to `docs/api/openapi.yaml`.

> **Do not edit `openapi.yaml` by hand.** Update the `@Operation`, `@ApiResponse`, and
> `@Parameter` annotations on the API interfaces instead, then re-generate.
